package com.ckentcorp.gghotels;

import javax.servlet.ServletException;

public class BadRequestException extends ServletException
{
	private static final long serialVersionUID = 1L;

	public BadRequestException()
	{
	}

	public BadRequestException(String message)
	{
		super(message);
	}

	public BadRequestException(Throwable rootCause)
	{
		super(rootCause);
	}

	public BadRequestException(String message, Throwable rootCause)
	{
		super(message, rootCause);
	}
}
