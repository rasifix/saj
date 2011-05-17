/*
 * Copyright 2011 Simon Raess
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rasifix.saj;

import java.io.IOException;

/**
 * Wraps an {@link IOException} as a {@link RuntimeException} thus not polluting the interface. 
 * 
 * @author rasifix
 */
public class RuntimeIOException extends RuntimeException {

	private static final long serialVersionUID = -1771794874583434514L;

	public RuntimeIOException(Throwable th) {
		super(th);
	}

	public RuntimeIOException(String msg, Throwable th) {
		super(msg, th);
	}

}
