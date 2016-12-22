package edu.nju.exam.filters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class BufServletOutputStream extends ServletOutputStream {
	ByteArrayOutputStream bufferedOut;
	public BufServletOutputStream() {
		bufferedOut = new ByteArrayOutputStream();
	}
	public void write(int i) throws IOException {
		bufferedOut.write(i);
	}
	public byte[] toByteArray() {
		return bufferedOut.toByteArray();
	}
	public void reset() {
		bufferedOut.reset();
	}
	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setWriteListener(WriteListener arg0) {
		// TODO Auto-generated method stub
		
	}
}
