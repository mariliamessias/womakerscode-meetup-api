package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.entity.Address;
import com.womakerscode.meetup.repository.AddressRepository;
import com.womakerscode.meetup.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class AddressServiceTest {

    @InjectMocks
    AddressServiceImpl addressService;

    @Mock
    AddressRepository repository;

    @Test
    @DisplayName("Should save an address")
    public void saveAddressTest() {
        LocalDateTime creationDate = LocalDateTime.now();
        //cenario
        Address address = buildAddress(creationDate);

        //execução
        when(repository.findById(any())).thenReturn(Optional.empty());
        when(repository.save(address)).thenReturn(buildAddress(creationDate));

        Address savedAddress = addressService.save(address);

        //assert
        assertThat(savedAddress.getCity()).isEqualTo("Test City");
        assertThat(savedAddress.getCountry()).isEqualTo("Test Country");
        assertThat(savedAddress.getNeighborhood()).isEqualTo("Test neighborhood");
        assertThat(savedAddress.getNumber()).isEqualTo(123);
        assertThat(savedAddress.getCreatedAt()).isEqualTo(creationDate);
    }

    @Test
    @DisplayName("Should find an address")
    public void fidAddress() {
        LocalDateTime creationDate = LocalDateTime.now();
        Long id = 1L;
        //cenario
        Address address = buildAddress(creationDate);

        //execução
        when(repository.findById(any())).thenReturn(Optional.of(address));

        Optional<Address> savedAddress = addressService.getAddressById(id);

        //assert
        assertThat(savedAddress.isPresent()).isTrue();
        assertThat(savedAddress.get().getCountry()).isEqualTo("Test Country");
        assertThat(savedAddress.get().getNeighborhood()).isEqualTo("Test neighborhood");
        assertThat(savedAddress.get().getNumber()).isEqualTo(123);
        assertThat(savedAddress.get().getCreatedAt()).isEqualTo(creationDate);
    }

    private Address buildAddress(LocalDateTime creationDate) {
        return Address.builder()
                .id(11L)
                .createdAt(creationDate)
                .city("Test City")
                .country("Test Country")
                .neighborhood("Test neighborhood")
                .number(123)
                .build();
    }
}
