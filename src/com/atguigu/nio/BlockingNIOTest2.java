package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

public class BlockingNIOTest2 {

	@Test
	public void client() {
		
		SocketChannel socketChannel = null;
		FileChannel inChannel = null;
		try {
			socketChannel = SocketChannel.open(new InetSocketAddress("localhost",9880));
			
			inChannel = FileChannel.open(Paths.get("/Users/zhenhua/Documents/otherfiles/Sala.jpg"), StandardOpenOption.READ);
			
			ByteBuffer src = ByteBuffer.allocate(1024);
			
			while(inChannel.read(src) != -1) {
				
				src.flip();
				socketChannel.write(src);
				src.clear();
			}
			
			socketChannel.shutdownOutput();
			//接收server端的数据
			int len;
			while((len=socketChannel.read(src)) != -1) {
				src.flip();
				System.out.println(src.toString());
				System.out.println(new String(src.array(),0,len));
				src.clear();
			}
			
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
			if(socketChannel != null) {
				try {
					socketChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	@Test
	public void server() {
		
		ServerSocketChannel serverChannel = null;
		SocketChannel socketChannel = null;
		FileChannel channel = null;
		
		try {
			serverChannel = ServerSocketChannel.open();
			serverChannel.bind(new InetSocketAddress(9880));
			
			channel = FileChannel.open(Paths.get("/Users/zhenhua/Documents/otherfiles/Sala2.jpg"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);
			
			socketChannel = serverChannel.accept();
			
			ByteBuffer byteBuf = ByteBuffer.allocate(1024);
			
			while(socketChannel.read(byteBuf) != -1) {
				byteBuf.flip();
				channel.write(byteBuf);
				byteBuf.clear();
			}
			
			byteBuf.put("server response OK哈！".getBytes());
			byteBuf.flip();
			socketChannel.write(byteBuf);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(channel != null) {
				
				try {
					channel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(socketChannel != null) {
				try {
					socketChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(serverChannel != null) {
				try {
					serverChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
