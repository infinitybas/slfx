package com.infinitybas.slfx.samples;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.infinitybas.slfx.SLFXConfig;

@Configuration
@Import(SLFXConfig.class) // VERY IMPORTANT! DON'T FORGET THIS IMPORT
public class SampleSLFXApplicationConfig {
	
	// Your configuration here

}
