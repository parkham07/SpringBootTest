package kr.co.parkham.service.mapper;

import kr.co.parkham.dto.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
	User getUserByEmp(String emp);
}
