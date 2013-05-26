package com.navchina.xzhou.blockingqueue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumerDemo
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		BlockingQueue<Product> q = new LinkedBlockingQueue<Product>();
		Producer producer1 = new Producer(q, "maker 1");
        Consumer consumer1 = new Consumer(q);
        new Thread(producer1).start();
        new Thread(consumer1).start();
	}

}

class Product
{
	private int sn;
	private String madeBy;

	public Product(int sn, String madeBy)
	{
		this.sn = sn;
		this.madeBy = madeBy;
	}

	@Override
	public String toString()
	{
		return "sn: " + sn + "\n" 
		    + "made by " + madeBy + "\n";
	}

}

class Producer implements Runnable
{
	private final BlockingQueue<Product> queue;
	private String maker;

	public Producer(BlockingQueue<Product> queue, String maker)
	{
		this.queue = queue;
		this.maker = maker;
	}

	@Override
	public void run()
	{
		while (true) {
			try {
				queue.put(genProduct());
                Thread.sleep(500);
			}
			catch (InterruptedException e) {
                System.out.println("queue overflow");
				e.printStackTrace();
			}
		}
	}

	Product genProduct()
	{
		return new Product((int) System.currentTimeMillis(), maker);
	}

}

class Consumer implements Runnable
{
	private final BlockingQueue<Product> queue;

	public Consumer(BlockingQueue<Product> queue)
	{
		this.queue = queue;
	}

	@Override
	public void run()
	{
		while (true) {
			try {
				consume(queue.take());
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	void consume(Product p)
	{
		System.out.println(p);
	}

}
