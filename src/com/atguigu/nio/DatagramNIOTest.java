package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Test;

public class DatagramNIOTest {

	@Test
	public void send() throws IOException {
		
		DatagramChannel gramChannel = DatagramChannel.open();
		
		gramChannel.configureBlocking(false);
		
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		Scanner sc = new Scanner(System.in);
		
		while(sc.hasNext()) {
			String str = sc.next();
			buf.put(str.getBytes());
			buf.flip();
			gramChannel.send(buf, new InetSocketAddress("localhost",9880));
			buf.clear();
		}
		sc.close();
	}
	
	@Test
	public void receive() throws IOException {
		
		DatagramChannel channel = DatagramChannel.open();
		
		channel.configureBlocking(false);
		
		channel.bind(new InetSocketAddress(9880));
		
		Selector selector = Selector.open();
		
		channel.register(selector, SelectionKey.OP_READ);
		
		Iterator<SelectionKey> iterator = null;
		SelectionKey sk = null;
		
		while(selector.select() > 0) {
			
			iterator = selector.selectedKeys().iterator();
			while(iterator.hasNext()) {
				sk = iterator.next();
				
				if(sk.isReadable()) {
					
					ByteBuffer buf = ByteBuffer.allocate(1024);
					channel.receive(buf);
					
					buf.flip();
					System.out.println(new String(buf.array(),0, buf.limit()));
					
					buf.clear();
				}
				
				iterator.remove();
			}
		}
		
		
	}
}
