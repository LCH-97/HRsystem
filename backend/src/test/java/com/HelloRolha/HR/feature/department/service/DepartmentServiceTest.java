package com.HelloRolha.HR.feature.department.service;

import com.HelloRolha.HR.feature.department.model.dto.create.CreateDepartmentReq;
import com.HelloRolha.HR.feature.department.model.dto.create.CreateDepartmentRes;
import com.HelloRolha.HR.feature.department.model.entity.Department;
import com.HelloRolha.HR.feature.department.repo.DepartmentRepository;
import com.HelloRolha.HR.feature.position.model.dto.create.CreatePositionReq;
import com.HelloRolha.HR.feature.position.model.dto.create.CreatePositionRes;
import com.HelloRolha.HR.feature.position.model.entity.Position;
import com.HelloRolha.HR.feature.position.repo.PositionRepository;
import com.HelloRolha.HR.feature.position.service.PositionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest
class DepartmentServiceTest {
    @Mock
    private DepartmentRepository departmentRepository;


    //    @Mock
//    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private DepartmentService departmentService;
//    @Test
//    void create() {
//        System.out.println("test");
//        Department department = Department.builder()
//                .id(1)
//                .build();
//        given(departmentRepository.save(any(Department.class))).willReturn(department);
//        CreateDepartmentReq request = CreateDepartmentReq.builder()
//                .departmentNum(100)
//                .departmentName("비서실")
//                .build();
//        //when
//        CreateDepartmentRes response = departmentService.create(request);
//        //then
////        assertNotNull(response.getId());
//        assertEquals(response.getId(),1);
//    }
}