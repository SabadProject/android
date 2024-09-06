package farayan.commons;

public interface ActionWith2Param<TInput1, TInput2>
{
	void execute(TInput1 input1, TInput2 input2);
}
