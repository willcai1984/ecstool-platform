package com.nhn.platform.qa.cwmtest.cwmautotest;

import org.junit.runner.RunWith; 
import org.junit.runners.Suite;

import com.nhn.platform.qa.cwmtest.firefox.*;
//import com.nhn.platform.qa.cwmtest.chrome.*;
//import com.nhn.platform.qa.cwmtest.ie.*;

@RunWith(Suite.class)

@Suite.SuiteClasses({ 
	SampleTest.class
//	HostTest.class,
//	FrameTest.class,
//	Start2End.class
})
public class AppTest{
}
