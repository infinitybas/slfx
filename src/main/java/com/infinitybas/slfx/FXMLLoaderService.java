package com.infinitybas.slfx;

import java.io.IOException;

public interface FXMLLoaderService {
	Object load(final String resource) throws IOException;
	
	Object load(final String resource, final ClassLoader classLoader) throws IOException;
	
	public boolean registerIntent(final String resource, final Intent intent);
}
