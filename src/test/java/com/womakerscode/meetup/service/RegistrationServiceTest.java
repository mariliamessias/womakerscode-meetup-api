package com.womakerscode.meetup.service;

import com.womakerscode.meetup.exception.BusinessException;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.repository.RegistrationRepository;
import com.womakerscode.meetup.service.impl.RegistrationServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class RegistrationServiceTest {

    RegistrationServiceImpl registrationService;

    @MockBean
    RegistrationRepository repository;

    @BeforeEach
    public void setUp() {
        this.registrationService = new RegistrationServiceImpl(repository);
    }

    @Test
    @DisplayName("Should save an registration")
    public void saveStudent() {

        //cenario
        Registration registration = createValidRegistration();

        //execução
        Mockito.when(repository.existsByRegistration(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(registration)).thenReturn(createValidRegistration());

        Registration savedRegistration = registrationService.save(registration);
        //assert
        Assertions.assertThat(savedRegistration.getId()).isEqualTo(101L);
        Assertions.assertThat(savedRegistration.getName()).isEqualTo("Marilia Messias");
        Assertions.assertThat(savedRegistration.getRegistration()).isEqualTo("001");
        Assertions.assertThat(savedRegistration.getDateOfRegistration()).isEqualTo(LocalDate.now());

    }

    @Test
    @DisplayName("Should throw BusinessException when try to save a new registration with a registration duplicated")
    public void shouldNotSaveAsRegistrationDuplicated() {

        //cenario
        Registration registration = createValidRegistration();
        Mockito.when(repository.existsByRegistration(Mockito.any())).thenReturn(true);

        // execução
        Throwable exception = Assertions.catchThrowable(() -> registrationService.save(registration));

        //asserts
        Assertions.assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Registration already created");

        Mockito.verify(repository, Mockito.never()).save(registration);

    }

    @Test
    @DisplayName("Should get an registration by id")
    public void getRegistrationById() {

        //cenario
        Long id = 11L;
        Registration registration = createValidRegistration();
        registration.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(registration));

        // execução
        Optional<Registration> foundRegistration = registrationService.getRegistrationById(id);

        //asserts
        Assertions.assertThat(foundRegistration.isPresent()).isTrue();
        Assertions.assertThat(foundRegistration.get().getId()).isEqualTo(id);
        Assertions.assertThat(foundRegistration.get().getName()).isEqualTo(registration.getName());
        Assertions.assertThat(foundRegistration.get().getRegistration()).isEqualTo(registration.getRegistration());
        Assertions.assertThat(foundRegistration.get().getDateOfRegistration()).isEqualTo(registration.getDateOfRegistration());

    }

    private Registration createValidRegistration() {
        return Registration.builder()
                .id(101L)
                .name("Marilia Messias")
                .dateOfRegistration(LocalDate.now())
                .registration("001")
                .build();
    }
}
