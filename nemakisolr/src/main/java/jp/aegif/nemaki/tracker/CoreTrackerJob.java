/*******************************************************************************
 * Copyright (c) 2013 aegif.
 * 
 * This file is part of NemakiWare.
 * 
 * NemakiWare is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * NemakiWare is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with NemakiWare.
 * If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     linzhixing(https://github.com/linzhixing) - initial API and implementation
 ******************************************************************************/
package jp.aegif.nemaki.tracker;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Job class of index tracking
 * @author linzhixing
 *
 */
public class CoreTrackerJob implements Job{
	
	public CoreTrackerJob(){
		super();
	}
	
	public void execute(JobExecutionContext jec) throws JobExecutionException{
		CoreTracker coreTracker = (CoreTracker) jec.getJobDetail().getJobDataMap().get("TRACKER");
		//TODO When using multi threads Job, kind of exclusive control will be required. 
		coreTracker.indexNodes("AUTO");
	}
	
}
