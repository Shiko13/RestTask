package com.epam.controller;

import com.epam.error.AccessException;
import com.epam.model.dto.TraineeDtoInput;
import com.epam.model.dto.TraineeDtoOutput;
import com.epam.model.dto.TraineeProfileDtoInput;
import com.epam.model.dto.TraineeSaveDtoOutput;
import com.epam.model.dto.TraineeUpdateDtoOutput;
import com.epam.model.dto.TraineeUpdateListDtoInput;
import com.epam.model.dto.TraineeUpdateListDtoOutput;
import com.epam.model.dto.TrainerForTraineeDtoOutput;
import com.epam.model.dto.TrainingTypeShortOutputDto;
import com.epam.service.TraineeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    @InjectMocks
    private TraineeController traineeController;

    @Mock
    private TraineeService traineeService;

    @Test
    void getByUsername_ShouldReturnTraineeDtoOutput() {
        String username = "testUser";
        String password = "testPassword";
        TraineeDtoOutput expectedOutput = createExpectedTraineeDtoOutput();

        when(traineeService.getByUsername(username, password)).thenReturn(expectedOutput);

        TraineeDtoOutput result = traineeController.getProfile(username, password);

        assertNotNull(result);
        assertEquals(expectedOutput, result);
    }

    @Test
    void save_ShouldReturnTraineeDtoOutput() {
        TraineeDtoInput traineeDtoInput = createTestTraineeDtoInput();
        TraineeSaveDtoOutput expectedOutput = createTraineeSaveDtoOutput();

        when(traineeService.save(traineeDtoInput)).thenReturn(expectedOutput);


        TraineeSaveDtoOutput result = traineeController.registration(traineeDtoInput);

        assertNotNull(result);
        assertEquals(expectedOutput, result);
    }


    @Test
    void updateProfile_ShouldReturnTraineeDtoOutput() {
        String username = "testUser";
        String password = "testPassword";
        TraineeProfileDtoInput traineeDtoInput = createTraineeProfileDtoInput();
        TraineeUpdateDtoOutput expectedOutput = createTraineeUpdateDtoOutput();

        when(traineeService.updateProfile(username, password, traineeDtoInput)).thenReturn(expectedOutput);

        TraineeUpdateDtoOutput result = traineeController.updateProfile(username, password, traineeDtoInput);

        assertNotNull(result);
        assertEquals(expectedOutput, result);
    }

    @Test
    void updateTrainerList_ShouldReturnTraineeDtoOutput() {
        String username = "testUser";
        String password = "testPassword";
        TraineeUpdateListDtoInput traineeDtoInput = createTraineeUpdateListDtoInput();
        TraineeUpdateListDtoOutput expectedOutput = createTraineeUpdateListDtoOutput();

        when(traineeService.updateTrainerList(username, password, traineeDtoInput)).thenReturn(expectedOutput);

        TraineeUpdateListDtoOutput result = traineeController.updateTrainerList(username, password, traineeDtoInput);

        assertNotNull(result);
        assertEquals(expectedOutput, result);
    }

    @Test
    void deleteByUsername_ShouldReturnNoContentResponse() {
        String username = "testUser";
        String password = "testPassword";

        doNothing().when(traineeService).deleteByUsername(username, password);

        traineeController.deleteByUsername(username, password);

        verify(traineeService).deleteByUsername(username, password);
    }

    @Test
    void deleteByUsername_ShouldThrowRSE() {
        String username = "testUser";
        String password = "testPassword";

        Mockito.doThrow(new AccessException("Unauthorized access"))
               .when(traineeService)
               .deleteByUsername(username, password);

        try {
            traineeController.deleteByUsername(username, password);
            fail("Expected ResponseStatusException not thrown");
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus());
        }
    }

    public TraineeDtoInput createTestTraineeDtoInput() {
        return TraineeDtoInput.builder().dateOfBirth(LocalDate.of(1990, 5, 15)).address("123 Main Street").build();
    }

    public TraineeProfileDtoInput createTraineeProfileDtoInput() {
        return TraineeProfileDtoInput.builder()
                                     .firstName("Nikki")
                                     .lastName("Benz")
                                     .dateOfBirth(LocalDate.of(1974, 5, 7))
                                     .address("Mooning street 72")
                                     .isActive(true)
                                     .build();
    }

    public TraineeUpdateListDtoInput createTraineeUpdateListDtoInput() {
        return TraineeUpdateListDtoInput.builder().trainersList(List.of("john.smith", "michel.past")).build();
    }

    public TraineeDtoOutput createExpectedTraineeDtoOutput() {
        TraineeDtoOutput traineeDtoOutput = new TraineeDtoOutput();
        traineeDtoOutput.setDateOfBirth(LocalDate.of(1990, 5, 15));
        traineeDtoOutput.setAddress("123 Main Street");

        return traineeDtoOutput;
    }

    public TraineeUpdateListDtoOutput createTraineeUpdateListDtoOutput() {
        TrainerForTraineeDtoOutput trainer1 = TrainerForTraineeDtoOutput.builder()
                                                                        .firstName("Amir")
                                                                        .lastName("Ali")
                                                                        .username("amir.ali")
                                                                        .specialization(
                                                                                new TrainingTypeShortOutputDto("Gym"))
                                                                        .build();

        TrainerForTraineeDtoOutput trainer2 = TrainerForTraineeDtoOutput.builder()
                                                                        .firstName("Helga")
                                                                        .lastName("Parks")
                                                                        .username("helga.parks")
                                                                        .specialization(
                                                                                new TrainingTypeShortOutputDto("Yoga"))
                                                                        .build();

        return TraineeUpdateListDtoOutput.builder().trainers(List.of(trainer1, trainer2)).build();
    }

    public TraineeSaveDtoOutput createTraineeSaveDtoOutput() {
        return TraineeSaveDtoOutput.builder().username("john.doe").password("password").build();
    }

    public TraineeUpdateDtoOutput createTraineeUpdateDtoOutput() {
        return TraineeUpdateDtoOutput.builder()
                                     .username("john.doe")
                                     .lastName("Doe")
                                     .firstName("John")
                                     .dateOfBirth(LocalDate.of(1919, 5, 7))
                                     .address("Orange street 139")
                                     .isActive(true)
                                     .build();
    }
}