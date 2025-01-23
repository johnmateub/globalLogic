package com.globallogic.usuarios.model.mapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.globallogic.usuarios.entity.User;
import com.globallogic.usuarios.model.PhoneRequest;
import com.globallogic.usuarios.model.UserRequest;
import com.globallogic.usuarios.model.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", imports = {LocalDate.class, LocalDateTime.class})
public interface UserMapper {

  ObjectMapper objectMapper = new ObjectMapper();

  @Mapping(target = "created", expression = "java( LocalDate.now() )")
  @Mapping(target = "lastLogin", expression = "java( LocalDateTime.now() )")
  @Mapping(target = "isActive", constant = "true")
  @Mapping(target = "phones", source= "phones", qualifiedByName = "convertPhonesToString")
  User toUser(UserRequest userRequest);

  @Named("convertPhonesToString")
  default String convertPhonesToString(List<PhoneRequest> phones) throws JsonProcessingException {
    return objectMapper.writeValueAsString(phones);
  }

  @Mapping(target = "phones", source = "phones", qualifiedByName = "buildPhoneRequest")
  @Mapping(target = "id", source = "idUser")
  UserResponse toUserResponse(User user);

  @Named("buildPhoneRequest")
  default List<PhoneRequest> buildPhoneRequest(String phones) throws JsonProcessingException {
    return objectMapper.readValue(phones, new TypeReference<>() {});
  }
  

}
