package com.blackstrawai.common;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityConverter {

  private final ModelMapper modelMapper;

  public EntityConverter(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public <S, T> T convertToEntity(S source, Class<T> targetClass) {
    return modelMapper.map(source, targetClass);
  }

  public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
    return source.stream()
        .map(element -> modelMapper.map(element, targetClass))
        .collect(Collectors.toList());
  }
}
