package com.epam.service;


import com.epam.model.dto.TraineeDtoInput;
import com.epam.model.dto.TraineeDtoOutput;
import com.epam.model.dto.TraineeProfileDtoInput;
import com.epam.model.dto.TraineeSaveDtoOutput;
import com.epam.model.dto.TraineeUpdateDtoOutput;
import com.epam.model.dto.TraineeUpdateListDtoInput;
import com.epam.model.dto.TraineeUpdateListDtoOutput;

public interface TraineeService {

    TraineeSaveDtoOutput save(TraineeDtoInput traineeDtoInput);

    TraineeDtoOutput getByUsername(String username, String password);

    TraineeUpdateDtoOutput updateProfile(String userName, String password, TraineeProfileDtoInput traineeDtoInput);

    TraineeUpdateListDtoOutput updateTrainerList(String username, String password, TraineeUpdateListDtoInput traineeDtoInput);

    void deleteByUsername(String username, String password);
}
