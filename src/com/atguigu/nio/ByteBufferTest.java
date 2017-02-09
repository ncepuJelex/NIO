package com.atguigu.nio;

import java.nio.ByteBuffer;

import org.junit.Test;

public class ByteBufferTest {

	@Test
	public void testBuffer() {
		//if you read the source code, may be you will learn more fast
		ByteBuffer buf = ByteBuffer.allocate(1024);
		/*
		 * java.nio.HeapByteBuffer[pos=0 lim=1024 cap=1024]
			0
			1024
			1024
		 */
		System.out.println(buf.mark());
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
		
		String s = "Jelex.Xu";
		buf.put(s.getBytes());
		
		System.out.println(buf.mark());
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
		
		buf.flip();
		System.out.println(buf.mark());
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
		
		byte[] dst = new byte[buf.limit()];
		buf.get(dst);
		System.out.println(new String(dst));
		System.out.println(buf.mark());
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
		
		buf.rewind();
		System.out.println(buf.mark());
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
		
		buf.clear();
		System.out.println(buf.mark());
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
	}
	
	@Test
	public void testMarkAndReset() {
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.put("Jelex.xu".getBytes());
		
		buf.flip();
		byte[] dst = new byte[buf.limit()];
		buf.get(dst, 0, 2);
		System.out.println(new String(dst));
		
		System.out.println(buf.position());
		
		
		buf.mark();
		
		buf.get(dst, 2, 2);
		System.out.println(new String(dst));
		System.out.println(buf.position());
		
		buf.reset();
		System.out.println(buf.position());
		
		if(buf.hasRemaining()) {
			System.out.println(buf.remaining());
		}
	}
	
	@Test
	public void testDirectBuffer() {
		ByteBuffer buf = ByteBuffer.allocateDirect(1024);
		boolean b = buf.isDirect();
		System.out.println(b);
	}
}
