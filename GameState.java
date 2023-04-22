
public enum GameState {
    pressContinue, cardOrLocationTileSelection, settlementPlacement, drawCard, endTurn;

    public GameState nextState() {
        switch (KingdomBuilderPanel.state) {
            case cardOrLocationTileSelection: return settlementPlacement;
            case settlementPlacement: return (KingdomBuilderPanel.players[KingdomBuilderPanel.currentPlayerID].getMandatorySettlementsLeft() == 0 ? drawCard : cardOrLocationTileSelection);
            default: return null;
        }
    }
}
