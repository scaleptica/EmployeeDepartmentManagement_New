package org.paytm.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.paytm.dto.DepartmentDto;
import org.paytm.entities.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DepartmentMapper {

  @Autowired
  private ModelMapper modelMapper;
  private TypeMap<DepartmentDto, Department> typeMapDTO;
  private TypeMap<Department, DepartmentDto> typeMapDTO2;

  @PostConstruct
  protected void onPostConstruct() {
    this.initializeTypeMaps();
  }

  //apply log4j

  private void initializeTypeMaps() {
    log.info("initializing mapper to map DepartmentDto to Department...");
    this.typeMapDTO = this.modelMapper.createTypeMap(DepartmentDto.class, Department.class)
        .addMapping(DepartmentDto::getId, Department::setId)
        .addMapping(DepartmentDto::getName, Department::setName)
        .addMapping(DepartmentDto::getRemarks, Department::setRemarks)
        .addMapping(DepartmentDto::getCreatedAt, Department::setCreatedAt)
        .addMapping(DepartmentDto::getUpdatedAt, Department::setUpdatedAt);

    log.info("initializing mapper to map Department to DepartmentDto...");
    this.typeMapDTO2 = this.modelMapper.createTypeMap(Department.class, DepartmentDto.class)
        .addMapping(Department::getId, DepartmentDto::setId)
        .addMapping(Department::getName, DepartmentDto::setName)
        .addMapping(Department::getRemarks, DepartmentDto::setRemarks)
        .addMapping(Department::getCreatedAt, DepartmentDto::setCreatedAt)
        .addMapping(Department::getUpdatedAt, DepartmentDto::setUpdatedAt);
  }

  public Department fromDtoToEntity(DepartmentDto dto) {
      if (Objects.isNull(dto)) {
          return null;
      }
      Department entity = this.typeMapDTO.map(dto);
    return entity;
  }

  public DepartmentDto fromEntityToDto(Department entity) {
      if (Objects.isNull(entity)) {
          return null;
      }
    return this.typeMapDTO2.map(entity);
  }

  public List<DepartmentDto> fromEntityToDtoList(
      List<Department> departmentList) { //used in department controller
    List<DepartmentDto> departmentDtoList = departmentList.stream() //mapper layer conversion
        .map(it -> fromEntityToDto(it))
        .collect(Collectors.toList());
    return departmentDtoList;
  }

    /*public Department setNewDetails(Department departmentGivenByUser, Department departmentInitial){    //write typemap3
        departmentInitial.setName(departmentGivenByUser.getName());
        departmentInitial.setRemarks(departmentGivenByUser.getRemarks());
        departmentInitial.setUpdatedAt(departmentGivenByUser.getUpdatedAt());
        departmentInitial.setCreatedAt(departmentGivenByUser.getCreatedAt());
        return departmentInitial;
    }*/

  //change
  public Department setNewDetails(Department departmentGivenByUser, Department departmentInitial) {
    //modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    modelMapper.map(departmentGivenByUser, departmentInitial);  //use custom mapper
    return departmentInitial;
  }
}
