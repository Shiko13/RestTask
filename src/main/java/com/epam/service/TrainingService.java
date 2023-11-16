package com.epam.service;

import com.epam.model.dto.TrainingDtoInput;
import com.epam.model.dto.TrainingForTraineeDtoOutput;
import com.epam.model.dto.TrainingForTrainerDtoOutput;
import com.epam.spec.TrainingTraineeSpecification;
import com.epam.spec.TrainingTrainerSpecification;

import java.util.List;

public interface TrainingService {

    void save(String username, String password, TrainingDtoInput trainingDtoInput);

    List<TrainingForTraineeDtoOutput> findByDateRangeAndTraineeUserName(                                                          TrainingTraineeSpecification specification);

    List<TrainingForTrainerDtoOutput> findByDateRangeAndTrainerUserName(
                                                                        TrainingTrainerSpecification specification);

}
