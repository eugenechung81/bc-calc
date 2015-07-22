package com.novus.bc;

import org.junit.Assert;
import org.junit.Test;

import com.novus.bc.BCCalc.BasicEvaluator;
import com.novus.bc.BCCalc.Evaluator;
import com.novus.bc.BCCalc.Parser;
import com.novus.bc.BCCalc.SimpleParser;

public class BCCalcTest
{
	static Parser<Integer> parser = new SimpleParser();
	static Evaluator<Integer> evaluator = new BasicEvaluator();

	@Test
	public void testBasic() throws Exception
	{
		String exp = "1+4";
		int result = evaluator.evaluate(parser.parse(exp));
		Assert.assertEquals(5, result);

		exp = "5/2";
		result = evaluator.evaluate(parser.parse(exp));
		Assert.assertEquals(2, result);
	}

	@Test
	public void testComplex() throws Exception
	{
		String exp = "5*4+2";
		int result = evaluator.evaluate(parser.parse(exp));
		Assert.assertEquals(22, result);

		exp = "2+5*4";
		result = evaluator.evaluate(parser.parse(exp));
		Assert.assertEquals(22, result);

		exp = "5+2-1*5+3";
		result = evaluator.evaluate(parser.parse(exp));
		Assert.assertEquals(5, result);
	}

}
