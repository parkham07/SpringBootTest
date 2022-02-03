package kr.co.parkham;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TestApplication {

	public static void main(String[] args) {
		// TODO:
		//  port 변경(단순 변경시 security에서 거절 에러가 발생)
		//  에러메세지 리턴 공용 코드 작성
		//  테스트 코드 작성
		//  기타 코드 정리
		//  패키지 구조 정리
		//  속도 개선
		//  lint 확인
		SpringApplication.run(TestApplication.class, args);
	}

}
