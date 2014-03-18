/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package perspectives.util;


import java.io.Serializable;

import perspectives.base.DataSource;
import perspectives.base.DataSourceFactory;

/**
 *
 * @author mershack
 */
public class TableDataFactory extends DataSourceFactory implements Serializable {
   @Override
	public DataSource create(String name) {
		return new TableData(name);
	}

	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "TableData";
	} 
}
