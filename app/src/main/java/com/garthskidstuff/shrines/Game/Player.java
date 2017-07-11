package com.garthskidstuff.shrines.Game;

/**
 * Created by garthupshaw1 on 7/3/17.
 *
 */

abstract class Player implements Runnable {
    protected Board board;
    protected Integer id;
    private Thread thread;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    void startTurn(Board board) {
        this.board = board;
        thread = new Thread(this);
        thread.start();
    }

    Board getTurnResult() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return board;
    }

    abstract void doInBackground();

    @Override
    public void run() {
        doInBackground();
    }
}
