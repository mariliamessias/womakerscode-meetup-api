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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class RegistrationServiceTest {

    RegistrationService registrationService;

    @MockBean
    RegistrationRepository repository;

    @BeforeEach
    public void setUp() {
        this.registrationService = new RegistrationServiceImpl(repository);
    }

    @Test
    @DisplayName("Should save an registration")
    public void saveStudentTest() {

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
    public void shouldNotSaveAsRegistrationDuplicatedTest() {

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
    public void getRegistrationByIdTest() {

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

    @Test
    @DisplayName("Should return empty when get an registration by id when doesn't exists")
    public void registrationNotFoundByIdTest() {

        //cenario
        Long id = 11L;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //execução
        Optional<Registration> registration = registrationService.getRegistrationById(id);

        //asserts
        Assertions.assertThat(registration.isPresent()).isFalse();

    }

    @Test
    @DisplayName("Should delete an student")
    public void deleteRegistrationTest() {

        //cenario
        Registration registration = Registration.builder().id(11L).build();

        //execução
        assertDoesNotThrow(() -> registrationService.delete(registration));

        //asserts
        Mockito.verify(repository, Mockito.times(1)).delete(registration);

    }

    @Test
    @DisplayName("Should update an registration")
    public void updateRegistrationTest() {
        //cenario
        Long id = 101L;
        Registration updatingRegistration = Registration.builder().id(id).build();

        //execução
        Registration updatedRegistration = createValidRegistration();
        updatedRegistration.setId(id);

        Mockito.when(repository.save(updatingRegistration)).thenReturn(updatedRegistration);
        Registration registration = registrationService.update(updatingRegistration);

        //asserts
        Assertions.assertThat(registration.getId()).isEqualTo(updatedRegistration.getId());
        Assertions.assertThat(registration.getName()).isEqualTo(updatedRegistration.getName());
        Assertions.assertThat(registration.getDateOfRegistration()).isEqualTo(updatedRegistration.getDateOfRegistration());
        Assertions.assertThat(registration.getRegistration()).isEqualTo(updatedRegistration.getRegistration());

    }

    @Test
    @DisplayName("Should filter registrations must by properties")
    public void findRegistrationTest() {
        // cenário
        Registration registration = createValidRegistration();

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Registration> listRegistrations = singletonList(registration);

        Page<Registration> page = new PageImpl<Registration>(singletonList(registration), pageRequest, 1);

        //execução
        Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(page);
        Page<Registration> result = registrationService.find(registration, pageRequest);

        //asserts
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getContent()).isEqualTo(listRegistrations);
        Assertions.assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        Assertions.assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should get an Registration model by registration attribute")
    public void getRegistrationByRegistrationAttributeTest() {

        // cenario
        String registrationAttribute = "1234";
        Mockito.when(repository.findByRegistration(registrationAttribute)).
                thenReturn(Optional.of(Registration.builder().id(11L).registration(registrationAttribute).build()));

        //execução
        Optional<Registration> registration = registrationService.getRegistrationByRegistrationAttribute(registrationAttribute);

        //asserts
        Assertions.assertThat(registration.isPresent()).isTrue();
        Assertions.assertThat(registration.get().getId()).isEqualTo(11L);
        Assertions.assertThat(registration.get().getRegistration()).isEqualTo(registrationAttribute);

        Mockito.verify(repository, Mockito.times(1)).findByRegistration(registrationAttribute);

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
