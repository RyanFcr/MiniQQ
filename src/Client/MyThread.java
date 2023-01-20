package Client;


import Controller.ControlOp;

public class MyThread extends Thread {
    public ControlOp loginOp;

    /**
     * start会调用run
     */
    @Override
    public void run() {
        loginOp = new ControlOp();
    }

    /**
     * 创建一个线程
     */
    public void createThread() {
        System.out.println("创建线程");
        MyThread thread = new MyThread();
        thread.start();
        System.out.println("线程已经被创建");
    }
}
