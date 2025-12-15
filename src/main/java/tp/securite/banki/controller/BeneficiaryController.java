package tp.securite.banki.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tp.securite.banki.domain.Beneficiary;
import tp.securite.banki.model.BeneficiaryDTO;
import tp.securite.banki.model.CreateBeneficiaryResponseDTO;
import tp.securite.banki.service.BeneficiaryService;
import tp.securite.banki.swagger.BeneficiaryControllerResponses;

import java.util.List;
import java.util.UUID;

// TODO: user BeneficiaryDTO instead

@RestController
@RequestMapping("/beneficiaries")
@RequiredArgsConstructor
@Tag(name = "Beneficiaries", description = "API endpoints for managing beneficiaries for fund transfers")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @GetMapping
    @Operation(
        summary = "Get all beneficiaries",
        description = "Retrieve all beneficiaries registered for the authenticated user"
    )
    @BeneficiaryControllerResponses.GetBeneficiariesListResponse
    public ResponseEntity<List<CreateBeneficiaryResponseDTO>> getBeneficiaries(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        List<Beneficiary> beneficiaries = beneficiaryService.getBeneficiaries(userId);
        List<CreateBeneficiaryResponseDTO> responseDTOS = beneficiaries.stream()
                .map(beneficiary -> CreateBeneficiaryResponseDTO.builder()
                        .id(beneficiary.getId())
                        .name(beneficiary.getName())
                        .bankName(beneficiary.getBankName())
                        .build()
                )
                .toList();

        return ResponseEntity.ok(responseDTOS);
    }

    @PostMapping
    @Operation(
        summary = "Create a new beneficiary",
        description = "Register a new beneficiary for the authenticated user to enable fund transfers"
    )
    @BeneficiaryControllerResponses.CreateBeneficiaryResponse
    public ResponseEntity<CreateBeneficiaryResponseDTO> createBeneficiary(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody BeneficiaryDTO requestDTO,
            HttpServletRequest request
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        String name = requestDTO.getName();
        String bankName = requestDTO.getBankName();
        UUID accountId = requestDTO.getId();

        Beneficiary createdBeneficiary = beneficiaryService.createBeneficiary(
                userId,
                name,
                bankName,
                accountId,
                request
        );

        CreateBeneficiaryResponseDTO responseDTO = CreateBeneficiaryResponseDTO.builder()
                .id(createdBeneficiary.getId())
                .name(createdBeneficiary.getName())
                .bankName(createdBeneficiary.getBankName())
                .build();

        return ResponseEntity.ok(responseDTO);
    }


}
