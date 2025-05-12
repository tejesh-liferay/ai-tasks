/**
 * @author Petteri Karttunen
 */
import React from 'react';
import { useTranslation } from 'react-i18next';

import { JsonEditor } from 'json-edit-react';

const JsonField = ({ className, helpText, label, onChange, parameterName, parameterValue }) => {
  const { t } = useTranslation();

  const handleChange = ({ newData }) => {
    onChange(parameterName, newData);
  };

  return (
    <div className={'form-group ' + (className ? className : '')}>
      <label htmlFor={parameterName}>{label ? label : t(parameterName + '-label')}</label>
      <JsonEditor
        data={parameterValue || {}}
        indent={2}
        collapse={2}
        collapseAnimationTime={150}
        maxWidth={'100%'}
        onEdit={handleChange}
        onAdd={handleChange}
        onDelete={handleChange}
      />
      <small className="form-text text-muted">
        {helpText ? helpText : t(parameterName + '-help')}
      </small>
    </div>
  );
};

export default JsonField;
