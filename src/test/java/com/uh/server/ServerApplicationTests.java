package com.uh.server;

import com.thebuzzmedia.exiftool.ExifTool;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ServerApplicationTests {

	@MockBean
	private ExifTool exifTool;

	@Test
	void contextLoads() {
	}

}
