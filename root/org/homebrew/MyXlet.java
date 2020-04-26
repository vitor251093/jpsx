package org.homebrew;

import java.io.*;

import java.util.*;

import java.awt.*;
import java.net.*;

import javax.media.*;

import javax.tv.xlet.*;

import org.bluray.ui.event.HRcEvent;

import org.dvb.event.UserEvent;

import org.havi.ui.*;

import org.jpsx.bootstrap.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jpsx.api.InvalidConfigurationException;
import org.jpsx.bootstrap.classloader.JPSXClassLoader;
import org.jpsx.bootstrap.configuration.MachineDefinition;
import org.jpsx.bootstrap.configuration.XMLMachineDefinitionParser;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.awt.*;


public class MyXlet implements Xlet, ControllerListener {

	private HScene scene;
	private List<Container> guis;
	private XletContext context;

	public void initXlet(XletContext context) {
		this.context = context;

		scene = HSceneFactory.getInstance().getDefaultHScene();

		try {
			Properties vars = new Properties();
			vars.put("image", "roms/yu-gi-oh.bin");

			String configFile = "jpsx.xml";
			String machineId = "launch";
			String log4jFile = "log4j.properties";

			// init log4j
			PropertyConfigurator.configure(log4jFile);

			log.info("configFile=" + configFile + " machineId=" + machineId);
			Element config;
			try {
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				config = builder.parse(new File(configFile)).getDocumentElement();
			} catch (IOException e) {
				log.error("Cannot open/read '" + configFile + "'", e);
				return;
			} catch (Exception e) {
				log.error("Cannot parse '" + configFile + "'", e);
				return;
			}

			try {
				MachineDefinition machineDefinition = new XMLMachineDefinitionParser().parse(config, machineId, vars);
				JPSXMachineLifecycle machine = JPSXClassLoader.newMachine(JPSXLauncher.class.getClassLoader(), machineDefinition);

				guis = machine.getComponents();
				guis.stream().forEach(gui -> {
					gui.setSize(1920, 1080); // BD screen size
					scene.add(gui, BorderLayout.CENTER);
				});

				machine.start();
			} catch (InvalidConfigurationException e) {
				log.error("Invalid Configuration", e);
			} catch (Throwable t) {
				log.error("Unexpected error", t);
			}

		} catch (Exception e) {
		}
		scene.validate();
	}

// Don't touch any of the code from here on.

	public void startXlet() {
		guis.stream().forEach(gui -> {
			gui.setVisible(true);
			scene.setVisible(true);
			gui.requestFocus();
		});
	}

	public void pauseXlet() {
		guis.stream().forEach(gui -> {
			gui.setVisible(false);
		});
	}

	public void destroyXlet(boolean unconditional) {
		guis.stream().forEach(gui -> {
			scene.remove(gui);
		});
		scene = null;
	}

	/**
	 * Subclasses should override this if they're interested in getting
	 * this event.
	 **/
	protected void numberKeyPressed(int value){}

	/**
	 * Subclasses should override this if they're interested in getting
	 * this event.
	 **/
	protected void colorKeyPressed(int value){}

	/**
	 * Subclasses should override this if they're interested in getting
	 * this event.
	 **/
	protected void popupKeyPressed(){}

	/**
	 * Subclasses should override this if they're interested in getting
	 * this event.
	 **/
	protected void enterKeyPressed(){}

	/**
	 * Subclasses should override this if they're interested in getting
	 * this event.
	 **/
	protected void arrowLeftKeyPressed(){}

	/**
	 * Subclasses should override this if they're interested in getting
	 * this event.
	 **/
	protected void arrowRightPressed(){}

	/**
	 * Subclasses should override this if they're interested in getting
	 * this event.
	 **/
	protected void arrowUpPressed(){}

	/**
	 * Subclasses should override this if they're interested in getting
	 * this event.
	 **/
	protected void arrowDownPressed(){}

	public void controllerUpdate(ControllerEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}