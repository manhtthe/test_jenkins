package com.web.bookingKol.domain.booking.services.impl;

import com.web.bookingKol.common.Enums;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.booking.dtos.BookingDetailDTO;
import com.web.bookingKol.domain.booking.dtos.BookingSingleReqDTO;
import com.web.bookingKol.domain.booking.dtos.BookingSingleResDTO;
import com.web.bookingKol.domain.booking.dtos.UpdateBookingReqDTO;
import com.web.bookingKol.domain.booking.mappers.BookingDetailMapper;
import com.web.bookingKol.domain.booking.mappers.BookingSingleResMapper;
import com.web.bookingKol.domain.booking.models.BookingRequest;
import com.web.bookingKol.domain.booking.models.Contract;
import com.web.bookingKol.domain.booking.repositories.BookingRequestRepository;
import com.web.bookingKol.domain.booking.repositories.ContractRepository;
import com.web.bookingKol.domain.booking.services.BookingRequestService;
import com.web.bookingKol.domain.booking.services.BookingValidationService;
import com.web.bookingKol.domain.booking.services.ContractService;
import com.web.bookingKol.domain.booking.services.SoftHoldBookingService;
import com.web.bookingKol.domain.file.dtos.FileUsageDTO;
import com.web.bookingKol.domain.file.mappers.FileUsageMapper;
import com.web.bookingKol.domain.file.models.File;
import com.web.bookingKol.domain.file.services.FileService;
import com.web.bookingKol.domain.kol.models.KolAvailability;
import com.web.bookingKol.domain.kol.models.KolProfile;
import com.web.bookingKol.domain.kol.repositories.KolAvailabilityRepository;
import com.web.bookingKol.domain.kol.repositories.KolProfileRepository;
import com.web.bookingKol.domain.kol.services.KolWorkTimeService;
import com.web.bookingKol.domain.payment.dtos.PaymentReqDTO;
import com.web.bookingKol.domain.payment.services.PaymentService;
import com.web.bookingKol.domain.payment.services.SePayService;
import com.web.bookingKol.domain.user.models.User;
import com.web.bookingKol.domain.user.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class BookingRequestServiceImpl implements BookingRequestService {
    @Autowired
    private BookingRequestRepository bookingRequestRepository;
    @Autowired
    private KolProfileRepository kolProfileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingSingleResMapper bookingSingleResMapper;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileUsageMapper fileUsageMapper;
    @Autowired
    private ContractService contractService;
    @Autowired
    private SePayService sePayService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private SoftHoldBookingService softHoldBookingService;
    @Autowired
    private BookingValidationService bookingValidationService;
    @Autowired
    private KolAvailabilityRepository kolAvailabilityRepository;
    @Autowired
    private KolWorkTimeService kolWorkTimeService;
    @Autowired
    private BookingDetailMapper bookingDetailMapper;
    @Autowired
    private ContractRepository contractRepository;

    @Transactional
    @Override
    public ApiResponse<PaymentReqDTO> createBookingSingleReq(UUID userId, BookingSingleReqDTO bookingRequestDTO, List<MultipartFile> attachedFiles) {
        // --- 1. Fetch main entities ---
        KolProfile kol = kolProfileRepository.findById(bookingRequestDTO.getKolId())
                .orElseThrow(() -> new EntityNotFoundException("Kol Id Not Found: " + bookingRequestDTO.getKolId()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User Id Not Found: " + userId));
        // --- 2. Delegate validation ---
        bookingValidationService.validateBookingRequest(bookingRequestDTO, kol);
        //Check hold slot
        if (!softHoldBookingService.checkAndReleaseSlot(kol.getId(),
                bookingRequestDTO.getStartAt(),
                bookingRequestDTO.getEndAt(),
                userId.toString())) {
            throw new IllegalArgumentException("The time slot is no longer held by you, please re-select!");
        }
        // --- 3. Create Booking Request ---
        BookingRequest newBookingRequest = new BookingRequest();
        UUID bookingRequestId = UUID.randomUUID();
        newBookingRequest.setId(bookingRequestId);
        newBookingRequest.setKol(kol);
        newBookingRequest.setUser(user);
        newBookingRequest.setDescription(bookingRequestDTO.getDescription());
        newBookingRequest.setLocation(bookingRequestDTO.getLocation());
        newBookingRequest.setStartAt(bookingRequestDTO.getStartAt());
        newBookingRequest.setEndAt(bookingRequestDTO.getEndAt());
        newBookingRequest.setStatus(Enums.BookingStatus.REQUESTED.name());
        newBookingRequest.setBookingType(Enums.BookingType.SINGLE.name());
        newBookingRequest.setCreatedAt(Instant.now());
        newBookingRequest.setFullName(bookingRequestDTO.getFullName());
        newBookingRequest.setEmail(bookingRequestDTO.getEmail());
        newBookingRequest.setPhone(bookingRequestDTO.getPhone());
        // --- 4. Handle File Attachments ---
        if (attachedFiles != null && !attachedFiles.isEmpty()) {
            for (MultipartFile file : attachedFiles) {
                File fileUploaded = fileService.getFileUploaded(userId, file);
                FileUsageDTO fileUsageDTO = fileService.createFileUsage(fileUploaded, bookingRequestId, Enums.TargetType.ATTACHMENTS.name(), false);
                newBookingRequest.getAttachedFiles().add(fileUsageMapper.toEntity(fileUsageDTO));
            }
        }
        // --- 5. Create Contract ---
        Contract contract = new Contract();
        bookingRequestRepository.saveAndFlush(newBookingRequest);
        if (bookingRequestDTO.getIsConfirmWithTerms() == true) {
            contract = contractService.createNewContract(newBookingRequest);
            newBookingRequest.getContracts().add(contract);
        }
        // --- 6. Initiate Payment ---
        String transferContent = contract.getId().toString();
        PaymentReqDTO paymentReqDTO = paymentService.initiatePayment(
                newBookingRequest,
                contract,
                sePayService.createQRCode(contract.getAmount(), transferContent),
                user,
                contract.getAmount()
        );
        paymentReqDTO.setTransferContent(transferContent);
        // --- Create KOL work time ---
        KolAvailability ka = kolAvailabilityRepository.findAvailability(kol.getId(), bookingRequestDTO.getStartAt(),
                bookingRequestDTO.getEndAt());
        kolWorkTimeService.createNewKolWorkTime(ka, newBookingRequest, Enums.BookingStatus.REQUESTED.name(),
                bookingRequestDTO.getStartAt(),
                bookingRequestDTO.getEndAt());
        // --- 7. Build and return response ---
        return ApiResponse.<PaymentReqDTO>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Booking Request successfully!"))
                .data(paymentReqDTO)
                .build();
    }

    @Override
    public ApiResponse<List<BookingSingleResDTO>> getAllSingleRequestAdmin(UUID kolId,
                                                                           UUID userId,
                                                                           String status,
                                                                           LocalDate startAt,
                                                                           LocalDate endAt,
                                                                           LocalDate createdAtFrom,
                                                                           LocalDate createdAtTo,
                                                                           int page,
                                                                           int size) {
        List<BookingSingleResDTO> bookingSingleResDTOPage = findAllWithCondition(kolId, userId, status, startAt, endAt, createdAtFrom, createdAtTo, page, size);
        return ApiResponse.<List<BookingSingleResDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("GET All Booking Request successfully!"))
                .data(bookingSingleResDTOPage)
                .build();
    }

    private List<BookingSingleResDTO> findAllWithCondition(UUID kolId,
                                                           UUID userId,
                                                           String status,
                                                           LocalDate startAt,
                                                           LocalDate endAt,
                                                           LocalDate createdAtFrom,
                                                           LocalDate createdAtTo,
                                                           int page,
                                                           int size) {
        Specification<BookingRequest> spec = Specification.allOf();
        spec = spec.and(((root, query, cb) -> cb.equal(root.get("bookingType"), Enums.BookingType.SINGLE.name())));
        if (kolId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("kol").get("id"), kolId));
        }
        if (userId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("id"), userId));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (startAt != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("startAt"),
                            startAt.atStartOfDay(ZoneOffset.UTC).toInstant()));
        }
        if (endAt != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("endAt"),
                            endAt.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant()));
        }
        if (createdAtFrom != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("createdAt"),
                            createdAtFrom.atStartOfDay(ZoneOffset.UTC).toInstant()));
        }
        if (createdAtTo != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("createdAt"),
                            createdAtTo.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant()));
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return bookingRequestRepository.findAll(spec, pageable)
                .map(bookingRequest -> bookingSingleResMapper.toDto(bookingRequest)).stream().toList();
    }

    @Override
    public ApiResponse<BookingDetailDTO> getDetailSingleRequestAdmin(UUID bookingRequestId) {
        BookingRequest bookingRequest = bookingRequestRepository.findByIdWithAttachedFiles(bookingRequestId);
        if (bookingRequest == null) {
            throw new EntityNotFoundException("Booking Request Not Found");
        }
        return ApiResponse.<BookingDetailDTO>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Get detail booking request successfully!"))
                .data(bookingDetailMapper.toDto(bookingRequest))
                .build();
    }

    @Override
    public ApiResponse<List<BookingSingleResDTO>> getAllSingleRequestUser(UUID userId, String status, LocalDate startAt, LocalDate endAt, LocalDate createdAtFrom, LocalDate createdAtTo, int page, int size) {
        List<BookingSingleResDTO> bookingSingleResList = findAllWithCondition(null, userId, status, startAt, endAt, createdAtFrom, createdAtTo, page, size);
        return ApiResponse.<List<BookingSingleResDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("GET All Booking Request successfully, userId: " + userId))
                .data(bookingSingleResList)
                .build();
    }

    @Override
    public ApiResponse<List<BookingSingleResDTO>> getAllSingleRequestKol(UUID kolId, String status, LocalDate startAt, LocalDate endAt, LocalDate createdAtFrom, LocalDate createdAtTo, int page, int size) {
        List<BookingSingleResDTO> bookingSingleResList = findAllWithCondition(kolId, null, status, startAt, endAt, createdAtFrom, createdAtTo, page, size);
        return ApiResponse.<List<BookingSingleResDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("GET All Booking Request successfully, kolId: " + kolId))
                .data(bookingSingleResList)
                .build();
    }

    @Override
    public ApiResponse<BookingDetailDTO> getDetailSingleRequestKol(UUID bookingRequestId, UUID kolId) {
        BookingRequest bookingRequest = bookingRequestRepository.findByIdWithAttachedFiles(bookingRequestId);
        if (bookingRequest == null) {
            throw new EntityNotFoundException("Booking Request Not Found");
        }
        if (!bookingRequest.getKol().getId().equals(kolId)) {
            throw new AuthorizationServiceException("You are not authorized to view this booking request");
        }
        return ApiResponse.<BookingDetailDTO>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Get detail booking request successfully!"))
                .data(bookingDetailMapper.toDto(bookingRequest))
                .build();
    }

    @Override
    public ApiResponse<BookingDetailDTO> getDetailSingleRequestUser(UUID bookingRequestId, UUID userId) {
        BookingRequest bookingRequest = bookingRequestRepository.findByIdWithAttachedFiles(bookingRequestId);
        if (bookingRequest == null) {
            throw new EntityNotFoundException("Booking Request Not Found");
        }
        if (!bookingRequest.getUser().getId().equals(userId)) {
            throw new AuthorizationServiceException("You are not authorized to view this booking request");
        }
        return ApiResponse.<BookingDetailDTO>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Get detail booking request successfully!"))
                .data(bookingDetailMapper.toDto(bookingRequest))
                .build();
    }

    @Transactional
    @Override
    public ApiResponse<BookingDetailDTO> updateBookingRequest(UUID userId, UUID bookingRequestId, UpdateBookingReqDTO updateBookingReqDTO, List<MultipartFile> attachedFiles, List<UUID> fileIdsToDelete) {
        BookingRequest bookingRequest = bookingRequestRepository.findByIdWithAttachedFiles(bookingRequestId);
        if (bookingRequest == null) {
            throw new EntityNotFoundException("Booking Request Not Found: " + bookingRequestId);
        }
        if (!bookingRequest.getUser().getId().equals(userId)) {
            throw new AuthorizationServiceException("You are not authorized to update this booking request");
        }
        if (bookingRequest.getUpdatedAt() != null) {
            throw new IllegalArgumentException("Booking request only can be updated once");
        }
        if (Instant.now().isAfter(bookingRequest.getStartAt().minus(24, ChronoUnit.HOURS))) {
            throw new IllegalArgumentException("Booking request only can be updated before 24 hours");
        }
        if (updateBookingReqDTO != null) {
            if (updateBookingReqDTO.getFullName() != null) {
                bookingRequest.setFullName(updateBookingReqDTO.getFullName());
            }
            if (updateBookingReqDTO.getPhone() != null) {
                bookingRequest.setPhone(updateBookingReqDTO.getPhone());
            }
            if (updateBookingReqDTO.getEmail() != null) {
                bookingRequest.setEmail(updateBookingReqDTO.getEmail());
            }
            if (updateBookingReqDTO.getLocation() != null) {
                bookingRequest.setLocation(updateBookingReqDTO.getLocation());
            }
            if (updateBookingReqDTO.getDescription() != null) {
                bookingRequest.setDescription(updateBookingReqDTO.getDescription());
            }
        }
        if (attachedFiles != null && !attachedFiles.isEmpty()) {
            for (MultipartFile file : attachedFiles) {
                File fileUploaded = fileService.getFileUploaded(userId, file);
                FileUsageDTO fileUsageDTO = fileService.createFileUsage(fileUploaded, bookingRequestId, Enums.TargetType.ATTACHMENTS.name(), false);
                bookingRequest.getAttachedFiles().add(fileUsageMapper.toEntity(fileUsageDTO));
            }
        }
        if (fileIdsToDelete != null) {
            bookingRequest.getAttachedFiles().removeIf(fileUsage ->
                    fileIdsToDelete.contains(fileUsage.getFile().getId()));
            fileService.deleteFile(fileIdsToDelete);
        }
//        bookingRequest.setUpdatedAt(Instant.now());
        bookingRequestRepository.saveAndFlush(bookingRequest);
        return ApiResponse.<BookingDetailDTO>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Update booking request successfully"))
                .data(bookingDetailMapper.toDto(bookingRequest))
                .build();
    }

    @Override
    public ApiResponse<BookingDetailDTO> cancelBookingRequest(UUID userId, UUID bookingRequestId) {
        BookingRequest bookingRequest = bookingRequestRepository.findByIdWithAttachedFiles(bookingRequestId);
        if (bookingRequest == null) {
            throw new EntityNotFoundException("Booking Request Not Found: " + bookingRequestId);
        }
        if (bookingRequest.getStatus().equalsIgnoreCase(Enums.BookingStatus.CANCELLED.name())) {
            throw new IllegalArgumentException("Booking request is already cancelled");
        }
        if (!bookingRequest.getUser().getId().equals(userId)) {
            throw new AuthorizationServiceException("You are not authorized to cancel this booking request");
        }
        bookingRequest.setStatus(Enums.BookingStatus.CANCELLED.name());
//        bookingRequest.setUpdatedAt(Instant.now());
        bookingRequestRepository.saveAndFlush(bookingRequest);
        Contract contract = contractRepository.findByRequestId(bookingRequestId);
        contract.setStatus(Enums.ContractStatus.CANCELLED.name());
        contractRepository.saveAndFlush(contract);
        return ApiResponse.<BookingDetailDTO>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Cancel booking request successfully"))
                .data(bookingDetailMapper.toDto(bookingRequest))
                .build();
    }
}
