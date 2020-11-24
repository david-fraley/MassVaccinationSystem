/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.apache.commons.lang3.StringUtils;

/**
 * Creates the status bar for the Mirth client application.
 */
public class StatusBar extends javax.swing.JPanel {
    private String timezoneText;
    private String serverTimeZone;
    private String localTimeZone;
    private long timeOffset;
    private Thread updater;

    /** Creates new form StatusBar */
    public StatusBar() {
        initComponents();
        workingText.setText("");
        StringBuilder statusBarText = new StringBuilder();
        statusBarText.append("Connected to: ");

        if (!StringUtils.isBlank(PlatformUI.ENVIRONMENT_NAME)) {
            statusBarText.append(PlatformUI.ENVIRONMENT_NAME);
            if (!StringUtils.isBlank(PlatformUI.SERVER_NAME)) {
                statusBarText.append(" - ");
            } else {
                statusBarText.append(" | ");
            }
        }
        if (!StringUtils.isBlank(PlatformUI.SERVER_NAME)) {
            statusBarText.append(PlatformUI.SERVER_NAME + " | ");
        }
        statusBarText.append(PlatformUI.SERVER_URL);
        serverLabel.setText(statusBarText.toString());
        serverLabel.setIcon(new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/server.png")));
        progressBar.setEnabled(false);
        progressBar.setForeground(UIConstants.JX_CONTAINER_BACKGROUND_COLOR);

        this.setBorder(new BevelBorder(BevelBorder.LOWERED));
    }

    public void shutdown() {
        if (updater != null && updater.isAlive()) {
            updater.interrupt();
        }
    }

    public void setWorking(boolean working) {
        progressBar.setIndeterminate(working);
    }

    public void setText(String text) {
        workingText.setText(text);
    }

    public String getText() {
        return workingText.getText();
    }

    public void setServerText(String serverText) {
        serverLabel.setText(serverText);
    }

    public String getServerText() {
        return serverLabel.getText();
    }

    public void setTimezoneText(String timezoneText) {
        this.timezoneText = timezoneText;
    }

    public void setServerTime(Calendar serverTime) {
        timeOffset = serverTime.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        serverTimeZone = serverTime.getTimeZone().getID();
        localTimeZone = Calendar.getInstance().getTimeZone().getID();

        shutdown();
        updater = new Thread() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                timezoneLabel.setText(convertLocalToServerTime() + " " + timezoneText);
                            }
                        });
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (InvocationTargetException e) {
                    }
                }
            }
        };
        updater.start();
    }

    private String convertLocalToServerTime() {
        TimeZone localTimeZone = TimeZone.getTimeZone(this.localTimeZone);
        TimeZone serverTimeZone = TimeZone.getTimeZone(this.serverTimeZone);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, (int) timeOffset);

        calendar.add(Calendar.MILLISECOND, localTimeZone.getRawOffset() * -1);
        if (localTimeZone.inDaylightTime(calendar.getTime())) {
            calendar.add(Calendar.MILLISECOND, localTimeZone.getDSTSavings() * -1);
        }

        calendar.add(Calendar.MILLISECOND, serverTimeZone.getRawOffset());
        if (serverTimeZone.inDaylightTime(calendar.getTime())) {
            calendar.add(Calendar.MILLISECOND, serverTimeZone.getDSTSavings());
        }

        return new SimpleDateFormat("h:mm a").format(calendar.getTime());
    }

    public void setStatusText(String statusText) {
        if (StringUtils.isEmpty(statusText)) {
            separator2Label.setText("");
            statusLabel.setText("");
        } else {
            separator2Label.setText("|");
            statusLabel.setText(statusText);
        }
    }

    public String getStatusText() {
        return statusLabel.getText();
    }

    // @formatter:off
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        serverLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        workingText = new javax.swing.JLabel();
        separator1Label = new javax.swing.JLabel();
        timezoneLabel = new javax.swing.JLabel();
        separator2Label = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        serverLabel.setText("server");

        progressBar.setDoubleBuffered(true);

        workingText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        workingText.setText("jLabel1");

        separator1Label.setText("|");

        timezoneLabel.setText("TZ");

        separator2Label.setText("|");

        statusLabel.setText("status");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(serverLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator1Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timezoneLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator2Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 136, Short.MAX_VALUE)
                .addComponent(workingText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(serverLabel)
                .addComponent(workingText)
                .addComponent(separator1Label)
                .addComponent(timezoneLabel)
                .addComponent(separator2Label)
                .addComponent(statusLabel))
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // @formatter:on

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel separator1Label;
    private javax.swing.JLabel separator2Label;
    private javax.swing.JLabel serverLabel;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel timezoneLabel;
    private javax.swing.JLabel workingText;
    // End of variables declaration//GEN-END:variables
}
