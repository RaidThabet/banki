package tp.securite.banki.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tp.securite.banki.domain.Beneficiary;
import tp.securite.banki.model.BeneficiaryDTO;
import tp.securite.banki.model.CreateBeneficiaryResponseDTO;
import tp.securite.banki.service.BeneficiaryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @GetMapping
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
    public ResponseEntity<CreateBeneficiaryResponseDTO> createBeneficiary(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody BeneficiaryDTO requestDTO
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        String name = requestDTO.getName();
        String bankName = requestDTO.getBankName();

        Beneficiary createdBeneficiary = beneficiaryService.createBeneficiary(
                userId,
                name,
                bankName
        );

        CreateBeneficiaryResponseDTO responseDTO = CreateBeneficiaryResponseDTO.builder()
                .id(createdBeneficiary.getId())
                .name(createdBeneficiary.getName())
                .bankName(createdBeneficiary.getBankName())
                .build();

        return ResponseEntity.ok(responseDTO);
    }


}
