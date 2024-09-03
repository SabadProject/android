package farayan.commons.Commons;

/**
 * Will be used in quick query form tools to send below messages
 */
public enum  QuickQueryFormActions
{
	/**
	 * when user types something and list of items must be filtered with entered text
	 */
	Query,

	/**
	 * when user taps on `Create` button or taps on `enter` key on keyboard, this message will be sent to request container to create
	 * related entity. if by any reason container can not create requested entity, must return false in response, if true was return, create button
	 * will be disabled due to existence (of created) entity
	 */
	Create,

	/**
	 * When user taps on `Create` button or `enter` on keyboard and QuickQueryForm component CAN create entity, QuickQueryForm
	 * component will create related entity and by sending this command will inform container to update list
	 */
	Created,

	/**
	 * When user taps on `Update` button or `enter` on keyboard and QuickQueryForm component CAN update entity, QuickQueryForm
	 * component will update related entity and by sending this command will inform container to update list
	 */
	Persisted,
}
