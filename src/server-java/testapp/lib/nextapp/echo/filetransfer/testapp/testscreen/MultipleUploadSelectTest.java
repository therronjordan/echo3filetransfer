/* 
 * This file is part of the Echo File Transfer Library.
 * Copyright (C) 2002-2009 NextApp, Inc.
 *
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 */

package nextapp.echo.filetransfer.testapp.testscreen;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import nextapp.echo.app.Border;
import nextapp.echo.app.Color;
import nextapp.echo.app.Column;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Insets;
import nextapp.echo.app.SplitPane;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.filetransfer.app.MultipleUploadSelect;
import nextapp.echo.filetransfer.app.event.UploadEvent;
import nextapp.echo.filetransfer.app.event.UploadListener;
import nextapp.echo.filetransfer.app.event.UploadProgressListener;
import nextapp.echo.filetransfer.model.Upload;
import nextapp.echo.filetransfer.receiver.JakartaUploadProcessor;
import nextapp.echo.filetransfer.testapp.ButtonColumn;
import nextapp.echo.filetransfer.testapp.InteractiveApp;

public class MultipleUploadSelectTest extends SplitPane {
    
    private MultipleUploadSelect uploadSelect;
    
    public MultipleUploadSelectTest() {
        setStyleName("TestControls");
        ButtonColumn controlsColumn = new ButtonColumn();
        controlsColumn.setStyleName("TestControlsColumn");
        add(controlsColumn);
        
        controlsColumn.addButton("Cancel", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uploadSelect.cancel();
                InteractiveApp.getApp().consoleWrite("Cancel");
            }
        });
        
        controlsColumn.addButton("Get Bandwidth", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InteractiveApp.getApp().consoleWrite("Bandwidth: " + JakartaUploadProcessor.getBandwidth());
            }
        });
        
        controlsColumn.addButton("Set Bandwidth = Unlimited", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JakartaUploadProcessor.setBandwidth(0);
            }
        });

        controlsColumn.addButton("Set Bandwidth = 128k", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JakartaUploadProcessor.setBandwidth(128 * 1024);
            }
        });

        controlsColumn.addButton("Set Bandwidth = 1MB", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JakartaUploadProcessor.setBandwidth(1024 * 1024);
            }
        });

        controlsColumn.addButton("Set Bandwidth = 16MB", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JakartaUploadProcessor.setBandwidth(16 * 1024 * 1024);
            }
        });

        // Some styleability tests
        controlsColumn.addButton("Set random foreground", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Random rnd = new Random();
                uploadSelect.setForeground(new Color(rnd.nextInt(), rnd.nextInt(), rnd.nextInt()));
            }
        });
        controlsColumn.addButton("Set random background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Random rnd = new Random();
                uploadSelect.setBackground(new Color(rnd.nextInt(), rnd.nextInt(), rnd.nextInt()));
            }
        });
        controlsColumn.addButton("Set Insets 10px", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uploadSelect.setInsets(new Insets(10));
            }
        });
        controlsColumn.addButton("Remove Insets", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uploadSelect.setInsets(null);
            }
        });
        controlsColumn.addButton("Set 500px width", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uploadSelect.setWidth(new Extent(500));
            }
        });
        controlsColumn.addButton("Remove width", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uploadSelect.setWidth(null);
            }
        });
        controlsColumn.addButton("Set 100px height", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uploadSelect.setHeight(new Extent(100));
            }
        });
        controlsColumn.addButton("Remove height", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uploadSelect.setHeight(null);
            }
        });
        controlsColumn.addButton("Set blue 5px border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uploadSelect.setBorder(new Border(5, Color.BLUE, Border.STYLE_SOLID));
            }
        });
        controlsColumn.addButton("Remove border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uploadSelect.setBorder(null);
            }
        });

        Column testColumn = new Column();
        
        uploadSelect = new MultipleUploadSelect();
        uploadSelect.setStyleName("Default");
        uploadSelect.addUploadProgressListener(new UploadProgressListener() {
        
            public void uploadStart(UploadEvent e) {
                System.err.println("START:" + e.getUpload().getFileName());
                if (("" + e.getUpload().getFileName()).toLowerCase().startsWith("a")) {
                    e.getUpload().cancel();
                }
            }
        
            public void uploadProgress(UploadEvent e) {
                System.err.println("PROGRESS:" + e.getUpload().getFileName() + ":" + e.getUpload().getProgress());
            }
        
            public void uploadCancel(UploadEvent e) {
                System.err.println("CANCEL");
            }
        });
        uploadSelect.addUploadListener(new UploadListener() {
        
            public void uploadComplete(UploadEvent e) {
                Upload upload = e.getUpload();
                switch (upload.getStatus()) {
                case Upload.STATUS_COMPLETE:
                    InteractiveApp.getApp().consoleWrite("Upload COMPLETE: " + e.getUpload());
                    InputStream in = null;
                    try {
                        byte[] buffer = new byte[4096];
                        int n = 0;
                        int bytes = 0;
                        in = e.getUpload().getInputStream();
                        while (-1 != (n = in.read(buffer))) {
                            bytes += n;
                        }
                        InteractiveApp.getApp().consoleWrite("Read: " + bytes + " bytes.");
                    } catch (IOException ex) {
                        InteractiveApp.getApp().consoleWrite("EXCEPTION: " + ex);
                    } finally {
                        try {
                            in.close();
                        } catch (IOException ex) {
                            InteractiveApp.getApp().consoleWrite("EXCEPTION: " + ex);
                        }
                    }                    
                    break;
                case Upload.STATUS_CANCELED:
                    InteractiveApp.getApp().consoleWrite("Upload CANCELED: " + e.getUpload());
                    break;
                case Upload.STATUS_ERROR_IO:
                    InteractiveApp.getApp().consoleWrite("Upload ERROR IO: " + e.getUpload());
                    break;
                case Upload.STATUS_ERROR_OVERSIZE:
                    InteractiveApp.getApp().consoleWrite("Upload ERROR OVERSIZE: " + e.getUpload());
                    break;
                case Upload.STATUS_IN_PROGRESS:
                    InteractiveApp.getApp().consoleWrite("Upload IN PROGRESS: " + e.getUpload());
                    break;
                default:
                    InteractiveApp.getApp().consoleWrite("Unexpected status: " + e.getUpload());
                    break;
                } 

            }

            @Override
            public void uploadSend() {
                InteractiveApp.getApp().consoleWrite("Upload SEND");
            }
        });
        testColumn.add(uploadSelect);
        add(testColumn);
    }
}
