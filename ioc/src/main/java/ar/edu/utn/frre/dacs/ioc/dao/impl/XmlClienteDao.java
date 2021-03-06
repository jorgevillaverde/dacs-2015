/*
 * Copyright (C) 2015 UTN-FRRe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ar.edu.utn.frre.dacs.ioc.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ar.edu.utn.frre.dacs.ioc.dao.ClienteDao;
import ar.edu.utn.frre.dacs.ioc.model.Cliente;

/**
 * Implementaci&oacute;n de un repositorio de Clientes de usando un archivo xml.
 * 
 * @author Jorge Villaverde
 * @version 1.0
 *
 */
public class XmlClienteDao implements ClienteDao {
	private static final String CLIENTE_TAG = "cliente";

	private String fileName;
	
	private List<Cliente> clientes;
	private Map<Long, Cliente> clientesMap;
	
	@Override
	public Cliente findById(Long id) {
		if(clientes == null)
			readClientesFromXml();
		return clientesMap.get(id);
	}

	private void readClientesFromXml() {
		
		clientes = new ArrayList<Cliente>();
		clientesMap = new HashMap<Long, Cliente>();
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			Document doc = docBuilder.parse (is); 
			doc.getDocumentElement ().normalize ();
			
			NodeList listOfClientes = doc.getElementsByTagName(CLIENTE_TAG);
			int totalPersons = listOfClientes.getLength();
			for(int s=0; s<totalPersons ; s++){
				Node clienteNode = listOfClientes.item(s);
				if(clienteNode.getNodeType() == Node.ELEMENT_NODE){
					Cliente cliente = new Cliente();
					
					Element clienteElement = (Element)clienteNode; 
					
					String id = getNodeString(clienteElement.getElementsByTagName("id"));
					String nombre = getNodeString(clienteElement.getElementsByTagName("nombre"));
					String apellido = getNodeString(clienteElement.getElementsByTagName("apellido"));
					String dni = getNodeString(clienteElement.getElementsByTagName("dni"));
					String telefono = getNodeString(clienteElement.getElementsByTagName("telefono"));
					String domicilio = getNodeString(clienteElement.getElementsByTagName("domicilio"));
					
					cliente.setId(Long.valueOf(id));
					cliente.setNombre(nombre);
					cliente.setApellido(apellido);
					cliente.setDni(Long.valueOf(dni));
					cliente.setTelefono(telefono);
					cliente.setDomicilio(domicilio);
					
					clientes.add(cliente);
					clientesMap.put(cliente.getId(), cliente);
				}
			}
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
		} catch (IOException e) {
		}
	}

	private static String getNodeString(NodeList firstNameList){
		Element firstNameElement = (Element)firstNameList.item(0); 
		NodeList textFNList = firstNameElement.getChildNodes();	
		return ((Node)textFNList.item(0)).getNodeValue().trim();
	}
	
	@Override
	public Cliente saveCliente(Cliente cliente) {
		throw new UnsupportedOperationException();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
