package com.HelloRolha.HR.feature.employee.service;

import com.HelloRolha.HR.config.utils.JwtUtils;
import com.HelloRolha.HR.error.TokenRefreshException;
import com.HelloRolha.HR.error.UserAccountException;
import com.HelloRolha.HR.error.UserNotFoundException;
import com.HelloRolha.HR.error.exception.CanNotInitException;
import com.HelloRolha.HR.feature.department.model.entity.Department;
import com.HelloRolha.HR.feature.employee.model.dto.EmployeeDto;
import com.HelloRolha.HR.feature.employee.model.dto.Login.LoginReq;
import com.HelloRolha.HR.feature.employee.model.dto.Login.LoginRes;
import com.HelloRolha.HR.feature.employee.model.dto.SignUp.SignUpReq;
import com.HelloRolha.HR.feature.employee.model.dto.SignUp.SignUpRes;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.repo.EmployeeRepository;
import com.HelloRolha.HR.feature.position.model.entity.Position;
import com.HelloRolha.HR.feature.refreshToken.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.secret-key2}")
    private String secretKey2;
    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;
    public SignUpRes signUp(SignUpReq signUpReq) {
        Employee employee= Employee.builder()
                .username(signUpReq.getUsername())
                .password(passwordEncoder.encode(signUpReq.getPassword()))
                .name(signUpReq.getName())
                .phoneNum(signUpReq.getPhoneNum())
                .birth(signUpReq.getBirth())
                .age(signUpReq.getAge())
                .address(signUpReq.getAddress())
                .employmentDate(LocalDate.now())
                .department(Department.builder().id(signUpReq.getDepartmentId()).build())
                .position(Position.builder().id(signUpReq.getPositionId()).build())
                .build();
        employeeRepository.save(employee);

        return SignUpRes.builder()
                .result(true)
                .build();
    }

    public LoginRes login(LoginReq loginReq) {
        Optional<Employee> result = employeeRepository.findByUsername(loginReq.getUsername());

        if(result.isEmpty()) {
            throw UserNotFoundException.forUsername(loginReq.getUsername());
        }

        Employee employee = result.get();
        if (passwordEncoder.matches(loginReq.getPassword(), employee.getPassword()) && employee.getStatus().equals(true)) {


            // refreshToken 생성
            String refreshToken = JwtUtils.generateRefreshToken(employee, secretKey2);
            try {
                // Redis에 refreshToken과 refreshTokenKey 저장
                redisTemplate.opsForValue().set("refreshToken:" + employee.getId(), refreshToken, expiredTimeMs, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                log.error("Redis 서버 접근 실패: ", e);
            }

            return LoginRes.builder()
                    .name(employee.getName())
                    .token(JwtUtils.generateAccessToken(employee, secretKey, expiredTimeMs))
                    .refreshToken(refreshToken)
                    .refreshTokenKey("refreshToken:" + employee.getId())
                    .build();


        } else {
            if(employee.getStatus().equals(true))
                throw UserAccountException.forInvalidPassword(loginReq.getPassword());
            else
                throw UserAccountException.forInvalidStatus(employee.getStatus());
        }
    }

    public List<EmployeeDto> listEmployee() {
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        List<Employee> employeeList = employeeRepository.getAllEmployList();
        for (Employee employee: employeeList) {
            employeeDtos.add(EmployeeDto.builder()
                    .id(employee.getId())
                    .name(employee.getName())
                    .employmentDate(employee.getEmploymentDate())
                    .department(employee.getDepartment().getDepartmentName())
                    .position(employee.getPosition().getPositionName())
                    .salary(employee.getSalary())
                    .build());
        }
        return employeeDtos;
    }
    public List<Employee> listEmployeeEntity() {

        List<Employee> employeeList = employeeRepository.findAll();

        return employeeList;
    }

    public Boolean authorize(Integer employeeId) {
        Optional<Employee> optionalEmployee =  employeeRepository.findById(employeeId);
        if(optionalEmployee.isEmpty()){
            throw UserNotFoundException.forIdx(employeeId);
        }
        Employee employee = optionalEmployee.get();
        employee.setStatus(true);
        employeeRepository.save(employee);
        return true;
    }
    public List<EmployeeDto> findAuthorize() {
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        List<Employee> employeeList = employeeRepository.findAllByStatus(false);
        for (Employee employee: employeeList) {
            employeeDtos.add(EmployeeDto.builder()
                    .id(employee.getId())
                    .name(employee.getName())
                    .employmentDate(employee.getEmploymentDate())
                    .department(employee.getDepartment().getDepartmentName())
                    .position(employee.getPosition().getPositionName())
                    .build());
        }
        return employeeDtos;
    }

    public EmployeeDto readEmployee(Integer employeeId) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if(employeeOptional.isEmpty()){
            throw UserNotFoundException.forIdx(employeeId);
        }
        Employee employee = employeeOptional.get();
        return EmployeeDto.builder()
                .id(employee.getId())
                .name(employee.getName())
                .employmentDate(employee.getEmploymentDate())
                .department(employee.getDepartment().getDepartmentName())
                .position(employee.getPosition().getPositionName())
                .build();
    }


    public Object createAdmin(SignUpReq signUpReq) {

        List<Employee> employeeOptional = employeeRepository.findALLByAuthority("ROLE_ADMIN");
        if(!employeeOptional.isEmpty()) {
            throw new CanNotInitException();
        }
        Employee employee= Employee.builder()
                .username(signUpReq.getUsername())
                .password(passwordEncoder.encode(signUpReq.getPassword()))
                .name(signUpReq.getName())
                .phoneNum(signUpReq.getPhoneNum())
                .birth(signUpReq.getBirth())
                .age(signUpReq.getAge())
                .address(signUpReq.getAddress())
                .employmentDate(LocalDate.now())
                .department(Department.builder().id(signUpReq.getDepartmentId()).build())
                .position(Position.builder().id(signUpReq.getPositionId()).build())
                .authority("ROLE_ADMIN")
                .status(true)
                .build();
        employeeRepository.save(employee);

        return SignUpRes.builder()
                .result(true)
                .build();
    }

    public List<EmployeeDto> listEmployeeByDepartmentId(Integer departmentId) {
        List<Employee> employees = employeeRepository.findByDepartmentId(departmentId);
        List<EmployeeDto> employeeDtos = new ArrayList<>();

        for (Employee employee : employees) {
            employeeDtos.add(EmployeeDto.builder()
                    .id(employee.getId())
                    .name(employee.getName())
                    .username(employee.getUsername())
                    .department(employee.getDepartment().getDepartmentName())
                    .position(employee.getPosition().getPositionName())
                    .build());
        }
        return employeeDtos;
    }
}
