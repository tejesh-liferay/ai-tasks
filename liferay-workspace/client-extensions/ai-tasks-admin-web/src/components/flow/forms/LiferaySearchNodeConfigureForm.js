/**
 * @author Louis-Guillaume Durand
 * @author Petteri Karttunen
 */
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';

import { TabContent } from '../../ui/TabContent';
import { Tab, Tabs } from '../../ui/Tabs';
import JsonField from './fields/JsonField';
import NumberField from './fields/NumberField';
import TextField from './fields/TextField';

const LiferaySearchNodeConfigureForm = ({ nodeParameters, onChange }) => {
  const [activeTab, setActiveTab] = useState(0);
  const { t } = useTranslation();

  return (
    <div>
      <div class="alert alert-info">{t('liferay-search-node-description')}</div>

      <Tabs onChange={setActiveTab}>
        <Tab id="generalSettings" label={t('general')} />
        <Tab id="inputOutputSettings" label={t('input-and-output')} />
      </Tabs>

      <TabContent activeTab={activeTab}>
        <div id="generalSettings">
          <div>
            <TextField
              onChange={onChange}
              parameterName="sxpBlueprintExternalReferenceCode"
              parameterValue={nodeParameters.sxpBlueprintExternalReferenceCode}
            />
            <TextField
              onChange={onChange}
              parameterName="documentResultField"
              parameterValue={nodeParameters.documentResultField}
            />
            <NumberField
              helpText="Enter the max number of search results to be returned."
              min="1"
              onChange={onChange}
              parameterName="topK"
              parameterValue={nodeParameters.topK}
            />
          </div>
        </div>
        <div id="inputOutputSettings">
          <div>
            <TextField
              onChange={onChange}
              parameterName="inputParameterName"
              parameterValue={nodeParameters.inputParameterName}
            />
            <TextField
              onChange={onChange}
              parameterName="outputParameterName"
              parameterValue={nodeParameters.outputParameterName}
            />
            <TextField
              onChange={onChange}
              parameterName="taskContextOutputParameterName"
              parameterValue={nodeParameters.taskContextOutputParameterName}
            />
          </div>
        </div>
      </TabContent>
    </div>
  );
};

export default LiferaySearchNodeConfigureForm;
