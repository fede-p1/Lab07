package it.polito.tdp.poweroutages.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		//System.out.println(model.getNercList());

		
		Nerc n = model.podao.getNercList().get(2);
		model.getOutages(n, 4, 200);
		
		System.out.println(model.getSoluzione().size());
		System.out.println(model.getSoluzione());
		
	}

}
