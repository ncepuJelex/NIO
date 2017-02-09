package com.atguigu.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedMap;

import org.junit.Test;

public class ChannelTest {

	@Test
	public void fun1() {
		
		FileInputStream in = null;
		FileOutputStream out = null;
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		
		try {
			in = new FileInputStream("/Users/zhenhua/Documents/otherfiles/Sala.jpg");
			out = new FileOutputStream("/Users/zhenhua/Documents/otherfiles/Sala2.jpg");
			
			inChannel = in.getChannel();
			outChannel = out.getChannel();
			
			ByteBuffer buf = ByteBuffer.allocate(1024);
			
			while(inChannel.read(buf) != -1) {
				buf.flip();
				
				outChannel.write(buf);
				
				buf.clear();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(inChannel != null) {
				
				try {
					inChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(outChannel != null) {
				try {
					outChannel.close();
				} catch (IOException e) {
				}
			}
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	public void fun2() throws IOException {
		
		FileChannel inChannel = FileChannel.open(Paths.get("/Users/zhenhua/Documents/otherfiles", "Sala.jpg"), StandardOpenOption.READ);
		
		FileChannel outChannel = FileChannel.open(Paths.get("/Users/zhenhua/Documents/otherfiles/Sala2.jpg"), StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		
		MappedByteBuffer inBuf = inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
		MappedByteBuffer outBuf = outChannel.map(MapMode.READ_WRITE, 0, inChannel.size());
		
		byte[] dst = new byte[inBuf.limit()];
		
		inBuf.get(dst);
		outBuf.put(dst);
		
		inChannel.close();
		outChannel.close();
	}
	
	@Test
	public void fun3() throws IOException {
		
		FileChannel inChannel = FileChannel.open(Paths.get("/Users/zhenhua/Documents/otherfiles", "Sala.jpg"), StandardOpenOption.READ);
		
		FileChannel outChannel = FileChannel.open(Paths.get("/Users/zhenhua/Documents/otherfiles/Sala2.jpg"), StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		
		
		inChannel.transferTo(0, inChannel.size(), outChannel);
		
		inChannel.close();
		outChannel.close();
	}
	
	@Test
	public void fun4() {
		
		RandomAccessFile file = null;
		FileChannel channel = null;
		try {
			file = new RandomAccessFile("/Users/zhenhua/Documents/otherfiles/linux学习.docx","rw");
			
			channel = file.getChannel();
			
			ByteBuffer buf1 = ByteBuffer.allocate(100);
			ByteBuffer buf2 = ByteBuffer.allocate(1024);
			ByteBuffer[] bufArr = {buf1, buf2};
			
			channel.read(bufArr);
			
			for(ByteBuffer buf : bufArr) {
				buf.flip();
			}
			
			System.out.println(new String(buf1.array()));
			System.out.println("==========");
			System.out.println(new String(buf2.array()));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(file != null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	public void fun5() {
	
		SortedMap<String,Charset> charsets = Charset.availableCharsets();
		Iterator<Entry<String, Charset>> iterator = charsets.entrySet().iterator();
		Entry<String, Charset> entry = null;
		while(iterator.hasNext()) {
			entry = iterator.next();
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}
	}
	
	@Test
	public void fun6() throws CharacterCodingException {
		Charset charset = Charset.forName("GBK");
		
		CharsetEncoder encoder = charset.newEncoder();
		CharsetDecoder decoder = charset.newDecoder();
		
		CharBuffer buf = CharBuffer.allocate(1024);
		buf.put("华北电力大学great!");
		
		buf.flip();
		
		ByteBuffer byteBuffer = encoder.encode(buf);
		
		System.out.println(byteBuffer.position());
		System.out.println(byteBuffer.limit());
		System.out.println();
		
		for(int i=0; i<18; i++) {
			System.out.println(byteBuffer.get());
		}
		byteBuffer.flip();
		for(int i=0; i<byteBuffer.limit()/2; i++) {
			System.out.println(byteBuffer.getChar());
		}
		
		byteBuffer.flip();
		CharBuffer decodeBuf = decoder.decode(byteBuffer);
		System.out.println(decodeBuf.toString());
		System.out.println(decodeBuf.remaining());
	}
	/*
	 * 普通方式读取
	 */
	@Test
	public void fun7() throws IOException {
		FileInputStream in = new FileInputStream("/Users/zhenhua/temp/test.txt");
		byte [] b = new byte[in.available()];
		in.read(b);
		System.out.println(new String(b,"GBK"));
		in.close();
	}
	/*
	 *nio方式读取
	 */
	@Test
	public void fun8() throws IOException {
		FileChannel fileChannel = FileChannel.open(Paths.get("/Users/zhenhua/temp/test.txt"), StandardOpenOption.READ);
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		StringBuffer sb = new StringBuffer();
		int len;
		while((len=fileChannel.read(buf)) != -1) {
			buf.flip();
			
			sb.append(new String(buf.array(),0,len,"GBK"));
			buf.clear();
		}
		System.out.println(sb.toString());
	}
	
}
