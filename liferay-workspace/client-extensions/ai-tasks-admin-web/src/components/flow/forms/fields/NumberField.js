/**
 * @author Petteri Karttunen
 */
import React from 'react';
import { useTranslation } from 'react-i18next';

import { enforceMinMax } from '../../../../utils/formUtils';

const NumberField = ({
  className,
  helpText,
  label,
  max,
  min,
  onChange,
  parameterName,
  parameterValue,
  required,
}) => {
  const { t } = useTranslation();

  return (
    <div className={'form-group ' + (className ? className : '')}>
      <label htmlFor={parameterName}>{label ? label : t(parameterName + '-label')}</label>
      <input
        className="form-control"
        id={parameterName}
        max={max ? max : ''}
        min={min ? min : ''}
        onChange={(e) => {
          enforceMinMax(e);
          onChange(parameterName, e.currentTarget.value);
        }}
        placeholder={t(parameterName + '-placeholder')}
        required={required ? 'required' : ''}
        type="number"
        value={parameterValue}
      />
      <small className="form-text text-muted">
        {helpText ? helpText : t(parameterName + '-help')}
      </small>
    </div>
  );
};

export default NumberField;
