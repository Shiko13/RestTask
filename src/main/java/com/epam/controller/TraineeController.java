package com.epam.controller;

import com.epam.error.AccessException;
import com.epam.model.dto.TraineeDtoInput;
import com.epam.model.dto.TraineeDtoOutput;
import com.epam.model.dto.TraineeProfileDtoInput;
import com.epam.model.dto.TraineeSaveDtoOutput;
import com.epam.model.dto.TraineeUpdateDtoOutput;
import com.epam.model.dto.TraineeUpdateListDtoInput;
import com.epam.model.dto.TraineeUpdateListDtoOutput;
import com.epam.service.TraineeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/trainee")
@Api(tags = "Trainee Controller")
public class TraineeController {

    private final TraineeService traineeService;

    @GetMapping("/username")
    @ApiOperation("Get trainee profile by username")
    public TraineeDtoOutput getProfile(@RequestParam String username, @RequestParam String password) {
        return traineeService.getByUsername(username, password);
    }

    @PostMapping()
    @ApiOperation("Trainee registration")
    public TraineeSaveDtoOutput registration(@RequestBody TraineeDtoInput traineeDtoInput) {
        return traineeService.save(traineeDtoInput);
    }

    @PutMapping("/profile")
    @ApiOperation("Update trainee profile")
    public TraineeUpdateDtoOutput updateProfile(@RequestParam String username, @RequestParam String password,
                                                @RequestBody TraineeProfileDtoInput traineeDtoInput) {
        return traineeService.updateProfile(username, password, traineeDtoInput);
    }

    @PutMapping("/trainer-list")
    @ApiOperation("Update trainee's trainer list")
    public TraineeUpdateListDtoOutput updateTrainerList(@RequestParam String username, @RequestParam String password,
                                                        @RequestBody TraineeUpdateListDtoInput traineeDtoInput) {
        return traineeService.updateTrainerList(username, password, traineeDtoInput);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Delete trainee by username")
    public void deleteByUsername(@RequestParam String username, @RequestParam String password) {
        try {
            traineeService.deleteByUsername(username, password);
        } catch (AccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
