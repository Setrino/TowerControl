/*
 * Sergei Kostevitch
 * Mar 10, 2012
 */

package message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageSender.
 */
public class MessageSender extends Thread {

	private final DataOutputStream dataOutputStream;

	private Message outputMessage = null;

	/** The priority queue. */
	private final List<Message> messagesList;

	/**
	 * Instantiates a new message sender.
	 * 
	 * @param outputStream
	 *            the output stream
	 */
	public MessageSender(DataOutputStream dataOutputStream) {

		this.dataOutputStream = dataOutputStream;
		messagesList = new LinkedList<Message>();
	}

	/**
	 * Adds the message to queue.
	 * 
	 * @param message
	 *            the message
	 */
	public synchronized void addMessageToQueue(Message message) {

		messagesList.add(message);
	}

	public Comparator<Message> comparator() {

		Comparator<Message> comparator = new Comparator<Message>() {

			@Override
			public int compare(Message m1, Message m2) {

				return m1.getPriority() < m2.getPriority() ? -1 : 1;
			}
		};

		return comparator;
	}

	public synchronized void restart() {

		System.out.println("Restarted");
		this.notify();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {

		while (true) {

			try {

				if (messagesList.size() != 0) {
					outputMessage = messagesList.get(0);
					// tower.removeMessageFromPriorityBlockingQueue(outputMessage);
					outputMessage.sendMessage(dataOutputStream);
					dataOutputStream.flush();
					messagesList.remove(0);
					outputMessage = null;

				} else {

					Thread.sleep(100);
					yield();

				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void stopThread() {

		try {
			this.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
