package com.atguigu.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

import org.junit.Test;

public class PipeTest {

	@Test
	public void fun() {
		
		Pipe pipe = null;
		Pipe.SinkChannel sinkChannel = null;
		Pipe.SourceChannel sourceChannel = null;
		
		try {
			pipe = Pipe.open();
			ByteBuffer buf = ByteBuffer.allocate(1024);
			
			buf.put("send message through the NIO pipe...哈".getBytes());
			buf.flip();
			
			sinkChannel = pipe.sink();
			sinkChannel.write(buf);
			
			/////
			
			sourceChannel = pipe.source();
			buf.flip();
			int len = sourceChannel.read(buf);
			System.out.println(new String(buf.array(),0,len));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(sinkChannel != null) {
				try {
					sinkChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(sourceChannel != null) {
				try {
					sourceChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
}
