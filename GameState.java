
public enum GameState {
    cardOrLocationTileSelection, cardOrLocationTileSelectionOrsettlementPlacement, settlementPlacement, drawCard, endTurn;

    public GameState nextState() {
        switch (KingdomBuilderPanel.state) {
            case cardOrLocationTileSelection: return cardOrLocationTileSelectionOrsettlementPlacement;
            case cardOrLocationTileSelectionOrsettlementPlacement: return settlementPlacement;
            case settlementPlacement: return (KingdomBuilderPanel.players[KingdomBuilderPanel.currentPlayerID].getMandatorySettlementsLeft() == 0 ? drawCard : cardOrLocationTileSelection);
            default: return null;
        }
    }
}
