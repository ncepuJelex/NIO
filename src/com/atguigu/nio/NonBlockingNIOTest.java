package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Test;

public class NonBlockingNIOTest {

	@Test
	public void client() {
		
		SocketChannel socketChannel = null;
		FileChannel inChannel = null;
		Scanner sc = null;
		
		try {
			socketChannel = SocketChannel.open(new InetSocketAddress("localhost",9880));
			
			socketChannel.configureBlocking(false);
			
			ByteBuffer byteBuf = ByteBuffer.allocate(1024);
			
			sc = new Scanner(System.in);
			
			while(sc.hasNext()) {
				String str = sc.next();
				byteBuf.put(str.getBytes());
//				byteBuf.put(("hello"+new Date()).getBytes());
				byteBuf.flip();
				socketChannel.write(byteBuf);
				byteBuf.clear();
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
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(sc != null) {
				sc.close();
			}
		}
	}
	
	
	@Test
	public void server() {
		
		ServerSocketChannel serverChannel = null;
		try {
			serverChannel = ServerSocketChannel.open();
			
			serverChannel.configureBlocking(false);
			
			serverChannel.bind(new InetSocketAddress(9880));
			
			Selector selector = Selector.open();
			
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			Iterator<SelectionKey> iterator = null;
			SelectionKey key = null;
			
			while(selector.select() > 0) {
				iterator = selector.selectedKeys().iterator();
				while(iterator.hasNext()) {
					key = iterator.next();
					
					if(key.isAcceptable()) {
						SocketChannel socketChannel = serverChannel.accept();
						socketChannel.configureBlocking(false);
						socketChannel.register(selector, SelectionKey.OP_READ);
					}
					else if(key.isReadable()) {
						SocketChannel socketChannel = (SocketChannel) key.channel();
						ByteBuffer buf = ByteBuffer.allocate(1024);
						
						int len = 0;
						while((len=socketChannel.read(buf)) > 0) {
							buf.flip();
							System.out.println(new String(buf.array(), 0, len));
							buf.clear();
						}
					}
					
					iterator.remove();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
