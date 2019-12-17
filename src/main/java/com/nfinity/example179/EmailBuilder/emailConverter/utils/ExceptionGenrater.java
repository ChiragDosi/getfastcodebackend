package com.nfinity.example179.EmailBuilder.emailConverter.utils;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.nfinity.example179.EmailBuilder.emailConverter.dto.response.ResponseDto;
import com.nfinity.example179.EmailBuilder.emailConverter.exception.DublicateValueException;
import com.nfinity.example179.EmailBuilder.emailConverter.exception.GenralException;


public class ExceptionGenrater {

	public static final BiFunction<String, Integer, Supplier<GenralException>> genralException = (message,
			status) -> () -> new GenralException(DataMapper.object2Json.apply(new ResponseDto<>(message, status)));

	public static final BiFunction<String, Integer, Supplier<DublicateValueException>> dublicateException = (message,
			status) -> () -> new DublicateValueException(
					DataMapper.object2Json.apply(new ResponseDto<>(message, status)));

}
