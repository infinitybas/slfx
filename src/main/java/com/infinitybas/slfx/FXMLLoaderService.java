package com.infinitybas.slfx;

import java.io.IOException;

public interface FXMLLoaderService {
	Object load(final String resource, Intent intent) throws IOException;
	
	Object load(final String resource, Intent intent, final ClassLoader classLoader) throws IOException;
}
