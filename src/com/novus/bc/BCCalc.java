package com.novus.bc;



public class BCCalc
{
	static enum Operator
	{
		ADD("+"), SUBTRACT("-"), MULTIPLY("*"), DIVIDE("/");

		private final String op;

		private Operator(String s)
		{
			op = s;
		}

		public String getOp()
		{
			return op;
		}

		public static Operator getEnum(String value)
		{
			for (Operator v : values())
				if (v.getOp().equalsIgnoreCase(value))
					return v;
			return null;
		}
	};

	static class Node<T>
	{
		Operator operator;
		Node<T> left;
		Node<T> right;
		T value;

		public Node(T value)
		{
			super();
			this.value = value;
		}

		public Node(Operator operator, Node<T> left, Node<T> right)
		{
			super();
			this.operator = operator;
			this.left = left;
			this.right = right;
		}

	}

	interface Parser<T>
	{
		Node<T> parse(String exp);
	}

	static class SimpleParser implements Parser<Integer>
	{
		@Override
		public Node<Integer> parse(String exp)
		{
			return parseExpression(new StringBuffer(exp));
		}

		private Node<Integer> parseExpression(StringBuffer sb)
		{
			Node<Integer> num = parseMultiDivExp(sb);
			Operator operator = getOperator(sb);
			if (Operator.ADD.equals(operator) || Operator.SUBTRACT.equals(operator))
			{
				sb.delete(sb.length() - 1, sb.length());
				Node<Integer> node = parseExpression(sb);
				return new Node<Integer>(operator, node, num);
			}
			return num;
		}

		private Node<Integer> parseMultiDivExp(StringBuffer sb)
		{
			Node<Integer> num = parseNumNode(sb);
			Operator operator = getOperator(sb);
			if (Operator.MULTIPLY.equals(operator) || Operator.DIVIDE.equals(operator))
			{
				sb.delete(sb.length() - 1, sb.length());
				Node<Integer> node = parseMultiDivExp(sb);
				return new Node<Integer>(operator, node, num);
			}
			return num;
		}

		private static Node<Integer> parseNumNode(StringBuffer sb)
		{
			for (int i = sb.length() - 1; i >= 0; i--)
			{
				if (sb.charAt(i) < '0' || sb.charAt(i) > '9')
				{
					String num = sb.substring(i + 1, sb.length());
					sb.delete(i + 1, sb.length());
					return new Node<Integer>(Integer.parseInt(num));
				}
			}
			return new Node<Integer>(Integer.parseInt(sb.toString()));
		}

		static Operator getOperator(StringBuffer sb)
		{
			String op = String.valueOf(sb.charAt(sb.length() - 1));
			return Operator.getEnum(op);
		}
	}

	interface Evaluator<T>
	{
		T evaluate(Node<T> root) throws Exception;
	}

	static class BasicEvaluator implements Evaluator<Integer>
	{
		@Override
		public Integer evaluate(Node<Integer> node) throws Exception
		{
			if (node.operator == null)
				return node.value;
			if (node.operator.equals(Operator.MULTIPLY))
				return evaluate(node.left) * evaluate(node.right);
			else if (node.operator.equals(Operator.DIVIDE))
				return evaluate(node.left) / evaluate(node.right);
			else if (node.operator.equals(Operator.ADD))
				return evaluate(node.left) + evaluate(node.right);
			else if (node.operator.equals(Operator.SUBTRACT))
				return evaluate(node.left) - evaluate(node.right);
			throw new Exception("Error evaluting");
		}
	}

	public static void main(String[] args) throws Exception
	{
		// String exp = "5+2-1*5+3";
		String exp = args[0];
	
		Parser<Integer> parser = new SimpleParser();
		Node<Integer> root = parser.parse(exp);
	
		Evaluator<Integer> evaluator = new BasicEvaluator();
		int result = evaluator.evaluate(root);
	
		System.out.println(result);
	}
}
