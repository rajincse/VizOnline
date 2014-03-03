package stimulusgen;

import java.util.ArrayList;

public class AggregatedUserData extends UserData {

	ArrayList< ArrayList<String>> fixationProvenance;
	ArrayList< ArrayList<String>> saccadeProvenance;
	
	public AggregatedUserData(String userId) {
		super(userId);

		fixationProvenance = new ArrayList< ArrayList<String>>();
		saccadeProvenance = new ArrayList< ArrayList<String>>();
	}
	
	public void addStimulus(Stimulus s)
	{
		if (stimuli.indexOf(s) >= 0)
			return;
		
		stimuli.add(s);
		fixations.add(new ArrayList<Fixation>());		
		saccades.add(new ArrayList<Saccade>());
		fixationProvenance.add(new ArrayList<String>());		
		saccadeProvenance.add(new ArrayList<String>());
		
	}
	
	public void addFixation(Stimulus s, Fixation f, String provenance)
	{
		super.addFixation(s, f);
		fixationProvenance.get(stimuli.indexOf(s)).add(provenance);
	}
	
	public void addSaccade(Stimulus s, Saccade f, String provenance)
	{
		super.addSaccade(s, f);
		saccadeProvenance.get(stimuli.indexOf(s)).add(provenance);
	}	
	
	

}
