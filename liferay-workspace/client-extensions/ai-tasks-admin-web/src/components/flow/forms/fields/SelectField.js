/**
 * @author Petteri Karttunen
 */
import React from 'react';
import { useTranslation } from 'react-i18next';

const SelectField = ({
  className,
  helpText,
  label,
  onChange,
  options,
  parameterName,
  parameterValue,
  required,
}) => {
  const { t } = useTranslation();

  return (
    <div className={'form-group ' + (className ? className : '')}>
      <label htmlFor={parameterName}>{label ? label : t(parameterName + '-label')}</label>
      <select
        className="form-control"
        id={parameterName}
        onChange={(e) => {
          onChange(parameterName, e.currentTarget.value);
        }}
        required={required ? 'required' : ''}
        type="text"
        value={parameterValue}
      >
        {options &&
          Object.entries(options).map(([key, value]) => <option value={key}>{value}</option>)}
      </select>
      <small className="form-text text-muted">
        {helpText ? helpText : t(parameterName + '-help')}
      </small>
    </div>
  );
};

export default SelectField;
