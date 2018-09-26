import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PrototypeMain extends Application {

	private ComboBox<RuleType> ruleTypeComboBox;
	private Pane rulePane;

	public void start( Stage primaryStage ) {
		Pane contentPane = new Pane();
		rulePane = new Pane();

		ruleTypeComboBox = new ComboBox<>( FXCollections.observableArrayList( RuleType.values() ) );
		ruleTypeComboBox.layoutXProperty().setValue( 10 );
		ruleTypeComboBox.layoutYProperty().setValue( 10 );
		ruleTypeComboBox.setValue( RuleType.values()[0] );
		updateRulePane();
		ruleTypeComboBox.setOnAction( e -> updateRulePane() );
		ruleTypeComboBox.setTooltip( new Tooltip( "Choose the type of rule to check for." ) );
		contentPane.getChildren().add( ruleTypeComboBox );

		rulePane.layoutXProperty()
				.bind( ruleTypeComboBox.layoutXProperty().add( ruleTypeComboBox.widthProperty() ).add( 10 ) );
		rulePane.layoutYProperty().bind( ruleTypeComboBox.layoutYProperty() );
		contentPane.getChildren().add( rulePane );

		TextField requiredPercentageTextField = new TextField();
		requiredPercentageTextField.layoutXProperty()
				.bind( rulePane.layoutXProperty().add( rulePane.widthProperty() ).add( 10 ) );
		requiredPercentageTextField.layoutYProperty().bind( rulePane.layoutYProperty() );
		requiredPercentageTextField.setTooltip( new Tooltip(
				"Set a percentage. The rule dictates that the current value must be within constraints for X% of "
						+ "the time within the given duration" ) );
		requiredPercentageTextField.prefWidthProperty().setValue( 100 );
		requiredPercentageTextField.setPromptText( "Percentage" );
		contentPane.getChildren().add( requiredPercentageTextField );

		Label percentLabel = new Label( "%" );
		percentLabel.layoutXProperty()
				.bind( requiredPercentageTextField.layoutXProperty().add( requiredPercentageTextField.widthProperty() )
						.add( 5 ) );
		percentLabel.layoutYProperty().bind( requiredPercentageTextField.layoutYProperty() );
		contentPane.getChildren().add( percentLabel );

		TextField durationTextField = new TextField();
		durationTextField.layoutXProperty()
				.bind( percentLabel.layoutXProperty().add( percentLabel.widthProperty() ).add( 10 ) );
		durationTextField.layoutYProperty().bind( percentLabel.layoutYProperty() );
		durationTextField.setTooltip( new Tooltip( "Set the duration the rule should compare against" ) );
		durationTextField.setPromptText( "Duration" );
		contentPane.getChildren().add( durationTextField );

		ComboBox<String> durationComboBox = new ComboBox<>( FXCollections.observableArrayList( "h", "d", "w", "m" ) );
		durationComboBox.layoutXProperty()
				.bind( durationTextField.layoutXProperty().add( durationTextField.widthProperty() ).add( 5 ) );
		durationComboBox.layoutYProperty().bind( durationTextField.layoutYProperty() );
		durationComboBox.setTooltip( new Tooltip( "Set the unit for the duration" ) );
		contentPane.getChildren().add( durationComboBox );

		Scene scene = new Scene( contentPane, 900, 100 );
		primaryStage.setScene( scene );
		primaryStage.show();
	}

	private void updateRulePane() {
		if ( rulePane.getChildren().size() > 0 ) {
			Pane oldPane = (Pane) rulePane.getChildren().get( 0 );
			oldPane.prefWidthProperty().unbind();
			oldPane.prefHeightProperty().unbind();
			rulePane.getChildren().clear();
		}
		Pane pane = ruleTypeComboBox.getValue().pane;
		pane.prefWidthProperty().bindBidirectional( rulePane.prefWidthProperty() );
		pane.prefHeightProperty().bindBidirectional( rulePane.prefHeightProperty() );
		rulePane.getChildren().add( pane );
	}

	private enum RuleType {
		LESS_THAN_GREATER_THAN( new LessThanGreaterThanPane() ), LESS_THAN( new LessThanPane() ), GREATER_THAN(
				new GreaterThanPane() );

		Pane pane;

		RuleType( Pane pane ) {
			this.pane = pane;
		}
	}

	private static class LessThanGreaterThanPane extends Pane {

		LessThanGreaterThanPane() {
			TextField lowerBoundTextField = new TextField();
			lowerBoundTextField.layoutXProperty().setValue( 0 );
			lowerBoundTextField.layoutYProperty().setValue( 0 );
			lowerBoundTextField.prefWidthProperty().setValue( 50 );
			lowerBoundTextField.setTooltip( new Tooltip( "Set the lower bound here" ) );
			lowerBoundTextField.setPromptText( "Lower Bound" );
			this.getChildren().add( lowerBoundTextField );

			Label label1 = new Label( "<" );
			label1.layoutXProperty()
					.bind( lowerBoundTextField.layoutXProperty().add( lowerBoundTextField.widthProperty() ).add( 10 ) );
			label1.layoutYProperty().bind( lowerBoundTextField.layoutYProperty() );
			this.getChildren().add( label1 );

			ComboBox<String> tagComboBox = new ComboBox<>(
					FXCollections.observableArrayList( "RPMs", "Temperature", "Pressure" ) );
			tagComboBox.layoutXProperty().bind( label1.layoutXProperty().add( label1.widthProperty() ).add( 10 ) );
			tagComboBox.layoutYProperty().bind( label1.layoutYProperty() );
			tagComboBox.setTooltip( new Tooltip( "Choose the tag" ) );
			this.getChildren().add( tagComboBox );

			Label label2 = new Label( "<" );
			label2.layoutXProperty().bind( tagComboBox.layoutXProperty().add( tagComboBox.widthProperty() ).add( 10 ) );
			label2.layoutYProperty().bind( tagComboBox.layoutYProperty() );
			this.getChildren().add( label2 );

			TextField upperBoundTextField = new TextField();
			upperBoundTextField.layoutXProperty()
					.bind( label2.layoutXProperty().add( label2.widthProperty() ).add( 10 ) );
			upperBoundTextField.layoutYProperty().bind( label2.layoutYProperty() );
			upperBoundTextField.prefWidthProperty().setValue( 50 );
			upperBoundTextField.setTooltip( new Tooltip( "Set the upper bound here" ) );
			upperBoundTextField.setPromptText( "Upper Bound" );
			this.getChildren().add( upperBoundTextField );
		}
	}

	private static class LessThanPane extends Pane {
		LessThanPane() {
			TextField lowerBoundTextField = new TextField();
			lowerBoundTextField.layoutXProperty().setValue( 0 );
			lowerBoundTextField.layoutYProperty().setValue( 0 );
			lowerBoundTextField.prefWidthProperty().setValue( 50 );
			lowerBoundTextField.setTooltip( new Tooltip( "Set the lower bound here" ) );
			lowerBoundTextField.setPromptText( "Lower Bound" );
			this.getChildren().add( lowerBoundTextField );

			Label label1 = new Label( "<" );
			label1.layoutXProperty()
					.bind( lowerBoundTextField.layoutXProperty().add( lowerBoundTextField.widthProperty() ).add( 10 ) );
			label1.layoutYProperty().bind( lowerBoundTextField.layoutYProperty() );
			this.getChildren().add( label1 );

			ComboBox<String> tagComboBox = new ComboBox<>(
					FXCollections.observableArrayList( "RPMs", "Temperature", "Pressure" ) );
			tagComboBox.layoutXProperty().bind( label1.layoutXProperty().add( label1.widthProperty() ).add( 10 ) );
			tagComboBox.layoutYProperty().bind( label1.layoutYProperty() );
			tagComboBox.setTooltip( new Tooltip( "Choose the tag" ) );
			this.getChildren().add( tagComboBox );
		}
	}

	private static class GreaterThanPane extends Pane {
		GreaterThanPane() {
			ComboBox<String> tagComboBox = new ComboBox<>(
					FXCollections.observableArrayList( "RPMs", "Temperature", "Pressure" ) );
			tagComboBox.layoutXProperty().setValue( 0 );
			tagComboBox.layoutYProperty().setValue( 0 );
			tagComboBox.setTooltip( new Tooltip( "Choose the tag" ) );
			this.getChildren().add( tagComboBox );

			Label label2 = new Label( "<" );
			label2.layoutXProperty().bind( tagComboBox.layoutXProperty().add( tagComboBox.widthProperty() ).add( 10 ) );
			label2.layoutYProperty().bind( tagComboBox.layoutYProperty() );
			this.getChildren().add( label2 );

			TextField upperBoundTextField = new TextField();
			upperBoundTextField.layoutXProperty()
					.bind( label2.layoutXProperty().add( label2.widthProperty() ).add( 10 ) );
			upperBoundTextField.layoutYProperty().bind( label2.layoutYProperty() );
			upperBoundTextField.prefWidthProperty().setValue( 50 );
			upperBoundTextField.setTooltip( new Tooltip( "Set the upper bound here" ) );
			upperBoundTextField.setPromptText( "Upper Bound" );
			this.getChildren().add( upperBoundTextField );
		}
	}
}
