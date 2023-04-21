
public enum GameState {
	pressContinue, cardOrLocationTileSelection, settlementPlacement, drawCard, endTurn;
	
	public GameState nextState() {
		switch (KingdomBuilderPanel.state) {
		case cardOrLocationTileSelection: return settlementPlacement;
		case settlementPlacement: return drawCard;
		default: return null;
		}
	}
}
